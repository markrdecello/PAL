
package com.bignerdranch.android.suicidepreventionapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
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

public class DailyFormTwo extends AppCompatActivity {
    private int id, answer, questionId;
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
        setContentView(R.layout.activity_daily_form_two);

        try {
            mSocket = IO.socket("http://pal.njcuacm.org:443");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mSocket.connect();

        final TextView question = findViewById(R.id.question);
        final Button submitFormButton = findViewById(R.id.submit_form_button);
        Button submitButton = findViewById(R.id.submit_button);
        submitFormButton.setVisibility(View.INVISIBLE);

        final RadioButton ten = findViewById(R.id.ten);
        final RadioButton eleven = findViewById(R.id.eleven);
        final RadioButton twelve = findViewById(R.id.twelve);
        final RadioButton thirteen = findViewById(R.id.thirteen);
        final RadioButton forteen = findViewById(R.id.forteen);
        final RadioButton fifteen = findViewById(R.id.fifteen);
        final RadioButton sixteen = findViewById(R.id.sixteen);

        SharedPreferences sharedPreferences = getSharedPreferences("dailyform",MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);

        final Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<List<Form>> call = api.getForm(id);
        call.enqueue(new Callback<List<Form>>() {
            @Override
            public void onResponse(Call<List<Form>> call, Response<List<Form>> response) {
                List<Form> forms = response.body();
                if (forms != null) {
                    question.setText(forms.get(1).getQuestion());
                    questionId = forms.get(1).getId();
                }

            }

            @Override
            public void onFailure(Call<List<Form>> call, Throwable t) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ten.isChecked()) {
                    answer = 10;
                } else if (eleven.isChecked()) {
                    answer = 11;
                } else if (twelve.isChecked()) {
                    answer = 12;
                } else if (thirteen.isChecked()) {
                    answer = 13;
                } else if (forteen.isChecked()) {
                    answer = 14;
                } else if (fifteen.isChecked()) {
                    answer = 15;
                } else if (sixteen.isChecked()) {
                    answer = 16;
                }
                System.out.println(questionId);
                Api secondApi = RetrofitClient.getRetrofit().create(Api.class);
                Call<AnswerResponse> secondCall = secondApi.answer(questionId,id, answer);
                secondCall.enqueue(new Callback<AnswerResponse>() {
                    @Override
                    public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {
                        submitFormButton.setVisibility(View.VISIBLE);
                        submitFormButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Api submitApi = RetrofitClient.getRetrofit().create(Api.class);
                                Call<Form> submitCall = submitApi.submitForm(id);
                                submitCall.enqueue(new Callback<Form>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Form> call, @NonNull Response<Form> response) {
                                        Toast.makeText(DailyFormTwo.this, "Thank You! You have submitted your form.", Toast.LENGTH_SHORT).show();
                                        mSocket.emit("add user", "Daily Form");
                                        startActivity(new Intent(DailyFormTwo.this, HostActivity.class));
                                        mSocket.disconnect();
                                    }

                                    @Override
                                    public void onFailure(Call<Form> call, Throwable t) {

                                    }
                                });
                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<AnswerResponse> call, Throwable t) {

                    }
                });
            }
        });

    }
}
