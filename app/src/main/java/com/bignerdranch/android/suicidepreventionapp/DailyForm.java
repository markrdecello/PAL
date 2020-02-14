package com.bignerdranch.android.suicidepreventionapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Models.AnswerResponse;
import com.bignerdranch.android.suicidepreventionapp.Models.Form;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyForm extends AppCompatActivity {
    private int id, answer, questionId;
    SharedPref sharedpref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedpref = new SharedPref(this);
        if(sharedpref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_form);

        final TextView question = findViewById(R.id.question);
        Button submitButton = findViewById(R.id.submit_button);
        final RadioButton three = findViewById(R.id.three);
        final RadioButton four = findViewById(R.id.four);
        final RadioButton five = findViewById(R.id.five);
        final RadioButton six = findViewById(R.id.six);
        final RadioButton seven = findViewById(R.id.seven);
        final RadioButton eight = findViewById(R.id.eight);
        final RadioButton nine = findViewById(R.id.nine);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("submitted id");
            }
        } else {
            id= (int) savedInstanceState.getSerializable("submitted id");
        }
        SharedPreferences sharedPreferences = getSharedPreferences("dailyform", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id",id).apply();
        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

        final Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<List<Form>> call = api.getForm(id);
        call.enqueue(new Callback<List<Form>>() {
            @Override
            public void onResponse(@NonNull Call<List<Form>> call, @NonNull Response<List<Form>> response) {
                List<Form> forms = response.body();
                if (forms != null) {
                    question.setText(forms.get(0).getQuestion());
                    questionId = forms.get(0).getId();
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Form>> call, @NonNull Throwable t) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (three.isChecked()) {
                    answer = 3;
                } else if (four.isChecked()) {
                    answer = 4;
                } else if (five.isChecked()) {
                    answer = 5;
                } else if (six.isChecked()) {
                    answer = 6;
                } else if (seven.isChecked()) {
                    answer = 7;
                } else if (eight.isChecked()) {
                    answer = 8;
                } else if (nine.isChecked()) {
                    answer = 9;
                }
                System.out.println(questionId);
                Api secondApi = RetrofitClient.getRetrofit().create(Api.class);
                Call<AnswerResponse> secondCall = secondApi.answer(questionId,id, answer);
                secondCall.enqueue(new Callback<AnswerResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerResponse> call, @NonNull Response<AnswerResponse> response) {
                        startActivity(new Intent(getApplicationContext(), DailyFormTwo.class));
                    }
                    @Override
                    public void onFailure(@NonNull Call<AnswerResponse> call, @NonNull Throwable t) {

                    }
                });
            }

        });
    }
}