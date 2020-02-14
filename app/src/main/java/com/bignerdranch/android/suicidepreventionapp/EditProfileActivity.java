package com.bignerdranch.android.suicidepreventionapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Counselor.TabbedActivity;
import com.bignerdranch.android.suicidepreventionapp.Models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private EditText profileName, profileEmail, profileBirthday, profilePhoneNumber, profileGender, profileGradYear, profileSchool, profileSchoolId, profileCounselorId;
    private String userId;
    User user;
    SharedPref sharedpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedpref = new SharedPref(this);
        if(sharedpref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        profileBirthday = findViewById(R.id.profile_birthday);
        profilePhoneNumber = findViewById(R.id.profile_phone_number);
        profileGender = findViewById(R.id.profile_gender);
        profileSchool = findViewById(R.id.profile_school);
        profileSchoolId = findViewById(R.id.profile_school_id);
        profileCounselorId = findViewById(R.id.profile_counselor_id);
        profileGradYear = findViewById(R.id.grad_year);
        final Button updateButton = findViewById(R.id.update_button);


        SharedPreferences sharedPreferences = getSharedPreferences("myLogin", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        userId = sharedPreferences.getString("user_id", "default");

        Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<User> call = api.getProfile(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                user = response.body();
                if (user != null) {
                    profileName.setText(user.getName());
                    profileEmail.setText(user.getEmail());
                    profileBirthday.setText(user.getBirth_date());
                    profilePhoneNumber.setText(user.getPhone_number());
                    profileGender.setText(user.getGender());
                    profileSchool.setText(user.getSchool());
                    profileSchoolId.setText(user.getSchool_id());
                    profileCounselorId.setText(user.getCounselor_id());
                    profileGradYear.setText(user.getGrad_year());
                    editor.putString("user_name", user.getMessage()).apply();

                    updateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateProfile();
                            if (user.getRole() == 1) {
                                Intent i = new Intent(EditProfileActivity.this, TabbedActivity.class);
                                startActivity(i);
                            }
                            else {
                                Intent i = new Intent(EditProfileActivity.this, HostActivity.class);
                                startActivity(i);
                            }
                        }
                    });


                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
                if (user.getRole() == 1) {
                    Intent i = new Intent(EditProfileActivity.this, TabbedActivity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(EditProfileActivity.this, HostActivity.class);
                    startActivity(i);
                }
            }
        });
    }
    private void updateProfile() {

        String profile_name = profileName.getText().toString();
        String profile_email = profileEmail.getText().toString();
        String profile_birthday = profileBirthday.getText().toString();
        String profile_phone_number = profilePhoneNumber.getText().toString();
        String profile_school = profileSchool.getText().toString();
        String profile_school_id = profileSchoolId.getText().toString();
        String profile_counselor_id = profileCounselorId.getText().toString();
        String profile_gender = profileGender.getText().toString();
        String profile_grad_year = profileGradYear.getText().toString();


        Api api = RetrofitClient.getRetrofit().create(Api.class);

        Call<User> mCall = api.update(userId, profile_name, profile_email, profile_birthday,
                profile_phone_number, profile_gender, profile_school, profile_school_id,
                profile_counselor_id, profile_grad_year
        );

        mCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                Toast.makeText(EditProfileActivity.this, "Your profile has been updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
