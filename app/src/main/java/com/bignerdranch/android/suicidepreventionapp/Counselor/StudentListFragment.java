package com.bignerdranch.android.suicidepreventionapp.Counselor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Adapters.ChatAdapter;
import com.bignerdranch.android.suicidepreventionapp.Api;
import com.bignerdranch.android.suicidepreventionapp.Models.Message;
import com.bignerdranch.android.suicidepreventionapp.Models.Student;
import com.bignerdranch.android.suicidepreventionapp.Models.User;
import com.bignerdranch.android.suicidepreventionapp.R;
import com.bignerdranch.android.suicidepreventionapp.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentListFragment extends Fragment {
    String counselorId;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.student_list, container, false);
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("myLogin", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        counselorId = sharedPreferences.getString("counselor_id", "default");
        int counselor_id = Integer.parseInt(counselorId);

        Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<List<Student>> call = api.getStudent(counselor_id);
        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(@NonNull Call<List<Student>> call, @NonNull Response<List<Student>> response) {
                List<Student> students = response.body();
                if (students != null) {
                    recyclerView = v.findViewById(R.id.chat_recycler_view);
                    layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setHasFixedSize(true);
                    ChatAdapter chatAdapter = new ChatAdapter(getActivity().getApplicationContext(), students);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(chatAdapter);

                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Student>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        return v;
    }
}