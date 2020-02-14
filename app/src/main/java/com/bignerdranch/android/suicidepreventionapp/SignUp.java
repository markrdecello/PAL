package com.bignerdranch.android.suicidepreventionapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Models.User;

import java.security.MessageDigest;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText firstName, lastName, email, password, confirmPassword, phoneNumber, counselorId, school, schoolId, graduationYear;
    private Button register, dateBirth;
    private Spinner gender;
    SharedPref sharedpref;
    String input;
    private DatePickerDialog.OnDateSetListener dateListener;
    private static final String TAG = "SignUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedpref = new SharedPref(this);
        if(sharedpref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Gender,
                R.layout.color_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        Spinner coloredSpinner = findViewById(R.id.gender);
        coloredSpinner.setAdapter(adapter);
        coloredSpinner.setOnItemSelectedListener(this);


        Typeface face = Typeface.createFromAsset(SignUp.this.getAssets(), "font/MPLUSRounded1c/MPLUSRounded1c-Medium.ttf");

        linkComponents();

        firstName.setTypeface(face);
        lastName.setTypeface(face);
        email.setTypeface(face);
        password.setTypeface(face);
        confirmPassword.setTypeface(face);
        phoneNumber.setTypeface(face);
        counselorId.setTypeface(face);
        school.setTypeface(face);
        schoolId.setTypeface(face);
        graduationYear.setTypeface(face);

        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        dateBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignUp.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        dateListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: yyyy-mm-dd: " + year + "-" + month + "-" + day);

                String date = year + "-" + month + "-"  + day;
                dateBirth.setText(date);
            }
        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!firstName.getText().toString().isEmpty()
                        && !lastName.getText().toString().isEmpty()
                        && !email.getText().toString().isEmpty()
                        && !dateBirth.getText().toString().isEmpty()
                        && !phoneNumber.getText().toString().isEmpty()
                        && !gender.getSelectedItem().toString().isEmpty()
                        && !school.getText().toString().isEmpty()
                        && !counselorId.getText().toString().isEmpty()
                        ) && (password.getText().toString().equals(confirmPassword.getText().toString())))
                {
                    if(password.getText().toString().length() < 6 && confirmPassword.getText().toString().length() < 6){
                        Toast.makeText(SignUp.this, "Make sure password is more than 6 characters.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        input = md5(password.getText().toString());
                        performRegistration();
                        Intent intent = new Intent(SignUp.this, Login.class);
                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(SignUp.this, "Please fill up all the required fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }

    private void linkComponents(){
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        dateBirth = findViewById(R.id.date_one);
        phoneNumber = findViewById(R.id.phone_number);
        gender = findViewById(R.id.gender);
        school = findViewById(R.id.school);
        schoolId = findViewById(R.id.school_id);
        counselorId = findViewById(R.id.counselor_id);
        graduationYear = findViewById(R.id.graduation_year);
        register = findViewById(R.id.register_button);
    }
    private void performRegistration()
    {
        String user_first_name = firstName.getText().toString();
        String user_last_name = lastName.getText().toString();
        String user_email = email.getText().toString();
        String user_password = input;
        String user_confirm_password = confirmPassword.getText().toString();
        String user_gender = gender.getSelectedItem().toString();
        String user_school = school.getText().toString();
        String user_school_id = schoolId.getText().toString();
        String user_graduation_year = graduationYear.getText().toString();
        String user_birth_date = dateBirth.getText().toString();
        String user_phone_number = phoneNumber.getText().toString();
        String user_counselor_id = counselorId.getText().toString();

        Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<User> call = api.register(user_first_name + " " + user_last_name, user_email, user_birth_date, user_password, user_school, user_school_id, user_graduation_year, user_gender, user_phone_number, user_counselor_id, 0);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                Toast.makeText(SignUp.this,"You have created an account.", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(SignUp.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}