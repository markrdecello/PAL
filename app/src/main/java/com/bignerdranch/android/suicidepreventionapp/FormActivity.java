package com.bignerdranch.android.suicidepreventionapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Models.AnswerResponse;
import com.bignerdranch.android.suicidepreventionapp.Models.Form;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormActivity extends AppCompatActivity {
    private  String[] questions;
    private int[] questionIds;
    private TextView question;
    private Button submitButton, submitFormButton;
    private RadioButton negativeTwo, negativeOne, zero, one, two;
    private RadioGroup radioGroup;
    private int answer;
    private int counter = 0;
    private  int id;
    private Socket mSocket;
    SharedPref sharedpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedpref = new SharedPref(this);
        if(sharedpref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        try {
            mSocket = IO.socket("http://pal.njcuacm.org:443");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mSocket.connect();

        question = findViewById(R.id.question);
        submitButton = findViewById(R.id.submit_button);
        negativeTwo = findViewById(R.id.negative_two);
        negativeOne = findViewById(R.id.negative_one);
        zero = findViewById(R.id.zero);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        radioGroup = findViewById(R.id.radio_group);
        submitFormButton = findViewById(R.id.submit_form_button);
        submitFormButton.setVisibility(View.INVISIBLE);

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

        SharedPreferences sharedPreferences = getSharedPreferences("myLogin", MODE_PRIVATE);
        final String name = sharedPreferences.getString("student_name", "default");


        Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<List<Form>> call = api.getForm(id);
        call.enqueue(new Callback<List<Form>>() {
            @Override
            public void onResponse(@NonNull Call<List<Form>> call, @NonNull Response<List<Form>> response) {
                List<Form> forms = response.body();
                assert forms != null;
                questions = new String[forms.size()];
                    questionIds = new int[forms.size()];
                    for (int i = 0; i < forms.size(); i++) {
                        questions[i] = forms.get(i).getQuestion();
                        questionIds[i] = forms.get(i).getId();
                        question.setText(questions[0]);

                        submitFormButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Api submitApi = RetrofitClient.getRetrofit().create(Api.class);
                                Call<Form> submitCall = submitApi.submitForm(id);
                                submitCall.enqueue(new Callback<Form>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Form> call, @NonNull Response<Form> response) {
                                        Toast.makeText(FormActivity.this, "Thank You! You have submitted your form.", Toast.LENGTH_SHORT).show();
                                        mSocket.emit("add user",  "Weekly Form");
                                        startActivity(new Intent(FormActivity.this, HostActivity.class));
                                    }

                                    @Override
                                    public void onFailure(Call<Form> call, Throwable t) {

                                    }
                                });

                            }
                        });

                        submitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                counter++;
                                if (negativeTwo.isChecked()){
                                    answer = -2;
                                }
                                else if (negativeOne.isChecked()){
                                    answer = -1;
                                }
                                else if (zero.isChecked()){
                                    answer = 0;
                                }
                                else if (one.isChecked()){
                                    answer = 1;
                                }
                                else if (two.isChecked()){
                                    answer = 2;
                                }
                                else {
                                    answer = 0;
                                }

                                if (counter < questions.length) {
                                    question.setText(questions[counter]);
                                    radioGroup.clearCheck();

                                }
                                else {
                                    Toast.makeText(FormActivity.this, "Thank You! You have completed the form.", Toast.LENGTH_SHORT).show();
                                    submitButton.setEnabled(false);
                                    submitFormButton.setVisibility(View.VISIBLE);

                                }

                                Api secondApi = RetrofitClient.getRetrofit().create(Api.class);
                                Call<AnswerResponse> secondCall = secondApi.answer(questionIds[counter -1],id, answer);

                                secondCall.enqueue(new Callback<AnswerResponse>() {
                                    @Override
                                    public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {
                                    }

                                    @Override
                                    public void onFailure(Call<AnswerResponse> call, Throwable t) {
                                        Toast.makeText(FormActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                        });

                        }
            }

            @Override
            public void onFailure(Call<List<Form>> call, Throwable t) {
                Toast.makeText(FormActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
