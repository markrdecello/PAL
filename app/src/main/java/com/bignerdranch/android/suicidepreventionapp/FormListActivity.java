package com.bignerdranch.android.suicidepreventionapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.bignerdranch.android.suicidepreventionapp.Models.SubmissionStatus;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormListActivity extends Fragment {
    private ListView listView;
    private String[] formNames;
    private int[] submissionIds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_form_list, container, false);

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("myLogin", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", "default");
        listView = view.findViewById(R.id.form_list);

        Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<List<SubmissionStatus>> mCall = api.getAvailableForms(user_id);

        mCall.enqueue(new Callback<List<SubmissionStatus>>() {
            @Override
            public void onResponse(@NonNull Call<List<SubmissionStatus>> call, @NonNull Response<List<SubmissionStatus>> response) {
                List<SubmissionStatus> statuses = response.body();
                if (statuses != null) {
                    formNames = new String[statuses.size()];
                    submissionIds = new int[statuses.size()];
                    for (int i = 0; i < statuses.size(); i++) {
                        formNames[i] = statuses.get(i).getName();
                        submissionIds[i] = statuses.get(i).getId();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.mytext, formNames);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (listView.getItemAtPosition(i).toString().equals("Weekly Form")) {
                                addIntent(submissionIds[i]);
                            }
                            else {
                                addSecondIntent(submissionIds[i]);
                            }
                        }
                    });
                }
                }
                @Override
                public void onFailure (Call < List < SubmissionStatus >> call, Throwable t){

                }
        });
        return view;
}

    private void addIntent(int submissionId){
        Intent myIntent = new Intent(getContext(), FormActivity.class);
        myIntent.putExtra("submitted id", submissionId);
        startActivity(myIntent);
    }

    private void addSecondIntent(int submissionId){
        Intent myIntent = new Intent(getContext(), DailyForm.class);
        myIntent.putExtra("submitted id", submissionId);
        startActivity(myIntent);
    }
}