package com.bignerdranch.android.suicidepreventionapp.Counselor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Api;
import com.bignerdranch.android.suicidepreventionapp.ChatRoom;
import com.bignerdranch.android.suicidepreventionapp.EditProfileActivity;
import com.bignerdranch.android.suicidepreventionapp.Models.User;
import com.bignerdranch.android.suicidepreventionapp.R;
import com.bignerdranch.android.suicidepreventionapp.RetrofitClient;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CounselorProfile extends Fragment {

    private TextView profileName, profileEmail, profileBirthday, profilePhoneNumber, profileSchool, profileSchoolId, profileCounselorId;
    private String counselorId;
    private int userRole;
    private Socket mSocket;
    String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.counselor_profile, container, false);

        try {
            mSocket = IO.socket("http://pal.njcuacm.org:443");
        }catch (URISyntaxException e){
            throw new RuntimeException(e);

        }

        mSocket.connect();

        profileName = v.findViewById(R.id.profile_name);
        profileEmail = v.findViewById(R.id.profile_email);
        profileBirthday = v.findViewById(R.id.profile_birthday);
        profilePhoneNumber = v.findViewById(R.id.profile_phone_number);
        profileSchool = v.findViewById(R.id.profile_school);
        profileSchoolId = v.findViewById(R.id.profile_school_id);
        profileCounselorId = v.findViewById(R.id.profile_counselor_id);
        Button editButton = v.findViewById(R.id.edit_button);


        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("myLogin", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        String userId = sharedPreferences.getString("user_id", "default");

        Api api = RetrofitClient.getRetrofit().create(Api.class);
        Call<User> call = api.getProfile(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                User user = response.body();
                if (user != null) {
                    profileName.setText(user.getName());
                    profileEmail.setText(user.getEmail());
                    profileBirthday.setText(user.getBirth_date());
                    profilePhoneNumber.setText(user.getPhone_number());
                    profileSchool.setText(user.getSchool());
                    profileSchoolId.setText(user.getSchool_id());
                    profileCounselorId.setText(user.getCounselor_id());
                    userRole = user.getRole();
                    counselorId = user.getCounselor_id();

                    editor.putString("user_name", user.getMessage()).apply();
                    editor.putString("counselor_id", counselorId).apply();
                    editor.putString("name", user.getName()).apply();
                    editor.putInt("role", userRole).apply();
                    editor.putString("name", user.getName()).apply();
                }
            }
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        final String cName = sharedPreferences.getString("name", "default");

        mSocket.on("user joined", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    String username = messageJson.getString("username");
                    if (username.equals("Weekly Form")){
                        showFormNotification("Form", "A student has submitted a weekly form.");
                    }
                    else if (username.equals("Daily Form")){
                        showFormNotification("Form", "A student has submitted a daily form.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        mSocket.on("new message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    String receiver = messageJson.getString("receiver");
                    String studentName = messageJson.getString("sender");
                    if (receiver.equals(cName)){
                        showNotification("New Notification", "You got a new message from " + studentName);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });




        //Buttons-------------------------------------------------------------------------------------------
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });
        return v;
    }
    void showNotification(String title, String content) {
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity().getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getActivity().getApplicationContext(), ChatRoom.class);
        PendingIntent pi = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

    void showFormNotification(String title, String content) {
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity().getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getActivity().getApplicationContext(), TabbedActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}