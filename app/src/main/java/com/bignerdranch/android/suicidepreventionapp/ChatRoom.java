package com.bignerdranch.android.suicidepreventionapp;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Adapters.ChatMessageAdapter;
import com.bignerdranch.android.suicidepreventionapp.Adapters.MessageListAdapter;
import com.bignerdranch.android.suicidepreventionapp.Counselor.CounselorChatRoom;
import com.bignerdranch.android.suicidepreventionapp.Models.ChatMessage;
import com.bignerdranch.android.suicidepreventionapp.Models.ChatResponse;
import com.bignerdranch.android.suicidepreventionapp.Models.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoom extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<ChatMessage> mMessages = new ArrayList<>();
    private EditText chatMessage;
    String username;
    private Socket socket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        mRecyclerView = findViewById(R.id.my_recycler_view);

        SharedPreferences sharedPreferences = getSharedPreferences("myLogin", MODE_PRIVATE);
        final String name = sharedPreferences.getString("student_name", "default");
        final  String counselorName = sharedPreferences.getString("counselor_name", "default");

        try {
            socket = IO.socket("http://pal.njcuacm.org:443");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }



        socket.connect();

        //---------- Displays all saved messages in the Chatroom------------------------------------
        Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<List<ChatResponse>> call = api.getSavedMessages( name, counselorName);
        call.enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChatResponse>> call, @NonNull Response<List<ChatResponse>> response) {
                List<ChatResponse> messages = response.body();
                if (!messages.get(0).getMessage().equals("nothing is found")) {
                    for (int i = 0; i < messages.size(); i++) {
                        if (messages.get(i).getSender().equals(name)) {
                            mMessages.add(new ChatMessage(messages.get(i).getMessage(), name));
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatRoom.this);
                            final ChatMessageAdapter messageAdapter = new ChatMessageAdapter(getApplicationContext(), mMessages);
                            mRecyclerView.setHasFixedSize(true);
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mRecyclerView.setAdapter(messageAdapter);
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                                }

                            });
                        } else {
                            mMessages.add(new ChatMessage(messages.get(i).getMessage(), counselorName));
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatRoom.this);
                            final ChatMessageAdapter messageAdapter = new ChatMessageAdapter(getApplicationContext(), mMessages);
                            mRecyclerView.setHasFixedSize(true);
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mRecyclerView.setAdapter(messageAdapter);
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<ChatResponse>> call, @NonNull Throwable t) {

            }
        });

        //------------------------------------------------------------------------------------------

        socket.on("new message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    String chatName = messageJson.getString("name");
                    String message = messageJson.getString("message");

                    if (chatName.equals(counselorName + name)) {
                        mMessages.add(new ChatMessage(message, "Sergio Ramos"));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatRoom.this);
                            final ChatMessageAdapter messageAdapter = new ChatMessageAdapter(getApplicationContext(), mMessages);
                            mRecyclerView.setHasFixedSize(true);
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mRecyclerView.setAdapter(messageAdapter);
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                                }

                            });
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        chatMessage = findViewById(R.id.chat_message);


        chatMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                socket.emit("typing");
            }

            @Override
            public void afterTextChanged(Editable s) {
                socket.emit("stop typing");
            }
        });

        Button sendMessageButton = findViewById(R.id.send_message_button);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String chat_message = chatMessage.getText().toString();


                chat_message = chatMessage.getText().toString();
                Api api = RetrofitClient.getRetrofit().create(Api.class);
                Call<ChatResponse> call = api.sendMessage(chat_message, name,counselorName);
                final String finalChat_message = chat_message;
                call.enqueue(new Callback<ChatResponse>() {
                    @Override
                    public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                        ChatResponse ct = response.body();
                        if (ct != null && ct.getSender().equals(counselorName)) {
                            mMessages.add(new ChatMessage(finalChat_message, name));
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatRoom.this);
                            final ChatMessageAdapter messageAdapter = new ChatMessageAdapter(getApplicationContext(), mMessages);
                            mRecyclerView.setHasFixedSize(true);
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mRecyclerView.setAdapter(messageAdapter);
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                                }

                            });
                        }
                        chatMessage.setText("");

                    }

                    @Override
                    public void onFailure(Call<ChatResponse> call, Throwable t) {

                    }
                });

                mMessages.add(new ChatMessage(chatMessage.getText().toString(), name));
                chatMessage.setText("");

            }
        });


    }


    void showNotification(String title, String content) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                .setAutoCancel(true); // clear notification after click
       Intent intent = new Intent(getApplicationContext(), ChatRoom.class);
       PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
       mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
















 /*




 pi apiGetMessage = RetrofitClient.getRetrofit().create(Api.class);
        Call<Message> callGetMessage = apiGetMessage.getMessage(chatId);
        callGetMessage.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Message messageTwo = response.body();

                if (messageTwo != null) {
                    updatedAt = messageTwo.getUpdatedAt();

                    if (!testString.equals(updatedAt)) {
                        mMessages.add(messageTwo);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatRoom.this);
                        final MessageAdapter messageAdapter = new MessageAdapter(ChatRoom.this, mMessages);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(messageAdapter);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                            }
                        });

                    }
                    testString = updatedAt;
                }
            }
            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
            }
        });

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Looper.prepare();
                System.out.println("fkahfla");

            }
        });


        socket.on("login", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("I am logged in!!");

            }
        });
        socket.emit("add user", "sajan");


         Api api = RetrofitClient.getRetrofit().create(Api.class);
                Call<Message> call = api.sendMessage(chatMessage.getText().toString(), Integer.parseInt(counselorId), Integer.parseInt(studentId), chatId, role);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {

                        chatMessage.setText("");
                    }

                    @Override
                    public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {

                    }
                });



*/
