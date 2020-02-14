package com.bignerdranch.android.suicidepreventionapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException;
import java.util.Objects;

public class JoinChatRoom extends Fragment{
    int role;
    String counselorId, studentId;
    private Socket mSocket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.join_chat_room, container, false);

        try {
            mSocket = IO.socket("http://pal.njcuacm.org:443");
        }catch (URISyntaxException e){
            throw new RuntimeException(e);

        }

        mSocket.connect();

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("myLogin", Context.MODE_PRIVATE);
        counselorId = sharedPreferences.getString("counselor_id","default");
        studentId = sharedPreferences.getString("user_id", "default");
        role = sharedPreferences.getInt("role", 0);
        final String name = sharedPreferences.getString("student_name", "default");
        final CardView myCard = v.findViewById(R.id.my_card);
        myCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSocket.emit("add user", name);
                Intent intent = new Intent(getActivity().getApplicationContext(), ChatRoom.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        mSocket.connect();
    }
}
