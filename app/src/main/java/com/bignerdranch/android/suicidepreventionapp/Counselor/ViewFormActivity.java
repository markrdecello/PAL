package com.bignerdranch.android.suicidepreventionapp.Counselor;

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

import com.bignerdranch.android.suicidepreventionapp.Api;
import com.bignerdranch.android.suicidepreventionapp.Models.SubmittedForm;
import com.bignerdranch.android.suicidepreventionapp.R;
import com.bignerdranch.android.suicidepreventionapp.RetrofitClient;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewFormActivity extends Fragment {
    private ListView viewFormList;
    String[] studentNames;
    String[] formNames;
    String[] myArray;
    int[] submittedIds;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_view_form,container,false);

        viewFormList = v.findViewById(R.id.view_form_list);
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("myLogin", Context.MODE_PRIVATE);

        String counselorId = sharedPreferences.getString("counselor_id", "default");
        int counselor_id = Integer.parseInt(counselorId);


        Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<List<SubmittedForm>> call = api.getSubmittedForm(counselor_id);
        call.enqueue(new Callback<List<SubmittedForm>>() {
            @Override
            public void onResponse(@NonNull Call<List<SubmittedForm>> call, @NonNull Response<List<SubmittedForm>> response) {
                List<SubmittedForm> submittedForms = response.body();
                if (submittedForms != null) {
                    studentNames = new String[submittedForms.size()];
                    formNames = new String[submittedForms.size()];
                    myArray = new String[submittedForms.size()];
                    submittedIds = new int[submittedForms.size()];
                    for (int i = 0; i < submittedForms.size(); i++) {
                        submittedIds[i] = submittedForms.get(i).getId();
                        myArray[i] = "                         "
                                + submittedForms.get(i).getName()
                                + "\n \n                         "
                                + submittedForms.get(i).getFormName()
                                + "\n";

                    }
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.mytext, myArray);
                    viewFormList.setAdapter(arrayAdapter);
                    viewFormList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), AnsweredFormActivity.class);
                            intent.putExtra("submitted_id", submittedIds[i]);

                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<SubmittedForm>> call, Throwable t) {

            }
        });

        return v;
    }
}
