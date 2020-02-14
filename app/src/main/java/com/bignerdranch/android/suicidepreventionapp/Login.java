package com.bignerdranch.android.suicidepreventionapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Counselor.TabbedActivity;
import com.bignerdranch.android.suicidepreventionapp.Models.User;

import java.security.MessageDigest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {


    private EditText email, password;
    private SharedPreferences mSharedPreferences;
    private Switch night_switch;
    SharedPref sharedpref;
    String input;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {


        sharedpref = new SharedPref(this);
        if(sharedpref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        night_switch = findViewById(R.id.night_switch);
        if(sharedpref.loadNightModeState()==true){
            night_switch.setChecked(true);
        }
        night_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sharedpref.setNightModeState(true);
                    recreate();
                }
                else {
                    sharedpref.setNightModeState(false);
                    recreate();
                }
            }
        });

        Typeface face = Typeface.createFromAsset(Login.this.getAssets(), "font/MPLUSRounded1c/MPLUSRounded1c-Medium.ttf");
        TextView email_text = findViewById(R.id.email_text);
        TextView password_text = findViewById(R.id.password_text);

        email_text.setTypeface(face);
        password_text.setTypeface(face);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button signInButton = findViewById(R.id.sign_in_button);
        Button signUpButton = findViewById(R.id.sign_up_button);
        mSharedPreferences = getSharedPreferences("myLogin", MODE_PRIVATE);

        //Buttons------------------------------------------------------------------------------------------------------
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    input = md5(password.getText().toString());
                    loginUser();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private Boolean validate() {
        Boolean result = false;

        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        if (userEmail.isEmpty() && userPassword.isEmpty()) {
            Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;

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
            return ""; // Impossibru!
        }
    }


    private void loginUser() {
        String user_email = email.getText().toString();
        String user_password = input;

        Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<User> mCall = api.login(user_email, user_password);
        mCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                User user = response.body();
                if (user != null) {
                    if (user.getStatus().equals("1")) {
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString("user_id", user.getMessage()).apply();
                        if (user.getRole() == 0) {

                            Intent i = new Intent(Login.this, HostActivity.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(Login.this, TabbedActivity.class);
                            startActivity(i);
                        }
                    } else {
                        Toast.makeText(Login.this, "Log In Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}