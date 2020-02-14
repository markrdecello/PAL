package com.bignerdranch.android.suicidepreventionapp.Counselor;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Adapters.RecyclerViewAdapter;
import com.bignerdranch.android.suicidepreventionapp.Api;
import com.bignerdranch.android.suicidepreventionapp.Models.Form;
import com.bignerdranch.android.suicidepreventionapp.R;
import com.bignerdranch.android.suicidepreventionapp.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnsweredFormActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Form> myForms;
    int submitted_id;
    private String[] questions;
    private int[] answers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answered_form);

        submitted_id = getIntent().getIntExtra("submitted_id", 0);

        Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<List<Form>> call = api.getForm(submitted_id);
        call.enqueue(new Callback<List<Form>>() {
            @Override
            public void onResponse(@NonNull Call<List<Form>> call, @NonNull Response<List<Form>> response) {
                myForms = response.body();
                if (myForms != null) {
                    questions = new String[myForms.size()];
                    answers = new int[myForms.size()];
                    for (int i = 0; i < myForms.size(); i++) {

                        //array of questions and answers of submitted forms--------------------------------------------------
                        questions[i] = myForms.get(i).getQuestion();
                        answers[i] = myForms.get(i).getAnswer();
                    }

                    recyclerView = findViewById(R.id.recycler_view);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(new RecyclerViewAdapter(getApplicationContext(), myForms));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Form>> call, @NonNull Throwable t) {
                Toast.makeText(AnsweredFormActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}