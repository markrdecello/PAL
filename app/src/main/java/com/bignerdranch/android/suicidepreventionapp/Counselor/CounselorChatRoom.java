package com.bignerdranch.android.suicidepreventionapp.Counselor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Adapters.ChatMessageAdapter;
import com.bignerdranch.android.suicidepreventionapp.Adapters.MessageListAdapter;
import com.bignerdranch.android.suicidepreventionapp.Api;
import com.bignerdranch.android.suicidepreventionapp.ChatRoom;
import com.bignerdranch.android.suicidepreventionapp.Models.ChatMessage;
import com.bignerdranch.android.suicidepreventionapp.Models.ChatResponse;
import com.bignerdranch.android.suicidepreventionapp.Models.Message;
import com.bignerdranch.android.suicidepreventionapp.R;
import com.bignerdranch.android.suicidepreventionapp.RetrofitClient;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CounselorChatRoom extends AppCompatActivity {

    private RecyclerView counselorRecyclerView;
    private List<ChatMessage> myMessages = new ArrayList<>();
    private EditText cMessage;
    String username, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor_chat_room);

        cMessage = findViewById(R.id.counselor_chat_message);
        counselorRecyclerView = findViewById(R.id.counselor_recycler_view);
        Button sendMButton = findViewById(R.id.counselor_send_message_button);

        SharedPreferences sharedPreferences = getSharedPreferences("myLogin", MODE_PRIVATE);
        final String name = sharedPreferences.getString("name", "default");
        final String studentName = sharedPreferences.getString("student_name", "default");

        Socket socket;
        try {
            socket = IO.socket("http://pal.njcuacm.org:443");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        socket.connect();

        Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<List<ChatResponse>> call = api.getSavedMessages(name, studentName);
        call.enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChatResponse>> call, @NonNull Response<List<ChatResponse>> response) {
                List<ChatResponse> messages = response.body();
                assert messages != null;
                if (!messages.get(0).getMessage().equals("nothing is found")) {
                    for (int i = 0; i < messages.size(); i++) {
                        if (messages.get(i).getSender().equals(name)) {
                            myMessages.add(new ChatMessage(messages.get(i).getMessage(), name));
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CounselorChatRoom.this);
                            final ChatMessageAdapter messageAdapter = new ChatMessageAdapter(getApplicationContext(), myMessages);
                            counselorRecyclerView.setHasFixedSize(true);
                            counselorRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            counselorRecyclerView.setLayoutManager(linearLayoutManager);
                            counselorRecyclerView.setAdapter(messageAdapter);
                            counselorRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    counselorRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                                }

                            });
                        } else {
                            myMessages.add(new ChatMessage(messages.get(i).getMessage(), studentName));
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CounselorChatRoom.this);
                            final ChatMessageAdapter messageAdapter = new ChatMessageAdapter(getApplicationContext(), myMessages);
                            counselorRecyclerView.setHasFixedSize(true);
                            counselorRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            counselorRecyclerView.setLayoutManager(linearLayoutManager);
                            counselorRecyclerView.setAdapter(messageAdapter);
                            counselorRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    counselorRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                                }

                            });

                        }
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<ChatResponse>> call, @NonNull Throwable t) {
                Toast.makeText(CounselorChatRoom.this, "nope", Toast.LENGTH_SHORT).show();

            }
        });


        socket.on("user joined", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    username = messageJson.getString("username");
                    showNotification("New Notification", "A student just joined the chat room.");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        socket.on("new message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    String chatName = messageJson.getString("name");
                    username = messageJson.getString("username");
                    message = messageJson.getString("message");

                    if (chatName.equals(studentName + name)) {
                        myMessages.add(new ChatMessage(message, studentName));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CounselorChatRoom.this);
                            final ChatMessageAdapter messageAdapter = new ChatMessageAdapter(getApplicationContext(), myMessages);
                            counselorRecyclerView.setHasFixedSize(true);
                            counselorRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            counselorRecyclerView.setLayoutManager(linearLayoutManager);
                            counselorRecyclerView.setAdapter(messageAdapter);
                            counselorRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    counselorRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                                }

                            });
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        socket.on("typing", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            }
        });


        sendMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String chat_message = cMessage.getText().toString();
                Api api = RetrofitClient.getRetrofit().create(Api.class);
                Call<ChatResponse> call = api.sendMessage(chat_message, name, studentName);
                call.enqueue(new Callback<ChatResponse>() {
                    @Override
                    public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                        ChatResponse ct = response.body();
                        if (ct != null && ct.getSender().equals(studentName)) {
                            myMessages.add(new ChatMessage(chat_message, name));
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CounselorChatRoom.this);
                            final ChatMessageAdapter messageAdapter = new ChatMessageAdapter(getApplicationContext(), myMessages);
                            counselorRecyclerView.setHasFixedSize(true);
                            counselorRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            counselorRecyclerView.setLayoutManager(linearLayoutManager);
                            counselorRecyclerView.setAdapter(messageAdapter);
                            counselorRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    counselorRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                                }

                            });
                        }
                        cMessage.setText("");

                    }

                    @Override
                    public void onFailure(Call<ChatResponse> call, Throwable t) {

                    }
                });
                myMessages.add(new ChatMessage(cMessage.getText().toString(), name));
                cMessage.setText("");
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