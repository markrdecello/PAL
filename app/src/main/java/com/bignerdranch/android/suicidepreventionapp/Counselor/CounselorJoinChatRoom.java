package com.bignerdranch.android.suicidepreventionapp.Counselor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Api;
import com.bignerdranch.android.suicidepreventionapp.ChatRoom;
import com.bignerdranch.android.suicidepreventionapp.Login;
import com.bignerdranch.android.suicidepreventionapp.Models.Form;
import com.bignerdranch.android.suicidepreventionapp.Models.KnockResponse;
import com.bignerdranch.android.suicidepreventionapp.Models.Message;
import com.bignerdranch.android.suicidepreventionapp.R;
import com.bignerdranch.android.suicidepreventionapp.RetrofitClient;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CounselorJoinChatRoom extends AppCompatActivity {
    private String[] questions;
    int student_id, chat_id;
    String student_name,counselor_id;
    private ListView myList;
    private Button assignButton;
    private Socket mSocket;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counselor_assign_message);

        try {
            mSocket = IO.socket("http://pal.njcuacm.org:443");
        }catch (URISyntaxException e){
            throw new RuntimeException(e);

        }

        mSocket.connect();

        SharedPreferences sharedPreferences = getSharedPreferences("myLogin", Context.MODE_PRIVATE);
        CardView assignForm = findViewById(R.id.assign_form);
        CardView sendMessage = findViewById(R.id.send_message);
        myList = findViewById(R.id.my_list);
        assignButton = findViewById(R.id.assign_button);

        student_id = sharedPreferences.getInt("student_id", 0);
        student_name = sharedPreferences.getString("student_name", "default");
        counselor_id = sharedPreferences.getString("counselor_id", "default");
        final String name = sharedPreferences.getString("name", "default");

        assignButton.setVisibility(View.INVISIBLE);

        assignForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printForm(30);
                assignButton.setVisibility(View.VISIBLE);

            }
        });



        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api assignApi = RetrofitClient.getRetrofit().create(Api.class);
                Call<Form> assignCall = assignApi.assignUser(String.valueOf(student_id), 30);
                assignCall.enqueue(new Callback<Form>() {
                    @Override
                    public void onResponse(@NonNull Call<Form> call, @NonNull Response<Form> response) {
                        mSocket.emit("add user", student_name+ "WeeklyForm");
                        Toast.makeText(CounselorJoinChatRoom.this, student_name + " has been assigned the form.", Toast.LENGTH_SHORT).show();
                        mSocket.disconnect();

                    }
                    @Override
                    public void onFailure(@NonNull Call<Form> call, @NonNull Throwable t) {
                        Toast.makeText(CounselorJoinChatRoom.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSocket.emit("add user", name+student_name);
                Intent intent = new Intent(CounselorJoinChatRoom.this, CounselorChatRoom.class);
                startActivity(intent);

            }


        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent = new Intent(CounselorJoinChatRoom.this, Login.class);
                startActivity(intent);
                break;
        }
        return true;
    }


    private void printForm(int formId){
        Api api1 = RetrofitClient.getRetrofit().create(Api.class);
        Call<List<Form>> call1 = api1.getFormByIndex(formId);
        call1.enqueue(new Callback<List<Form>>() {
            @Override
            public void onResponse(@NonNull Call<List<Form>> call, @NonNull Response<List<Form>> response) {
                List<Form> forms = response.body();
                if (forms != null) {
                    questions = new String[forms.size()];
                    for (int i = 0; i < forms.size(); i++) {
                        questions[i] = forms.get(i).getQuestion();
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, questions);
                    myList.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Form>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
