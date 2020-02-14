package com.bignerdranch.android.suicidepreventionapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Counselor.CounselorJoinChatRoom;
import com.bignerdranch.android.suicidepreventionapp.Models.Form;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification_receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("myLogin", Context.MODE_PRIVATE);
        String studentId = sharedPreferences.getString("user_id", "default");
        int student_id = Integer.parseInt(studentId);

        Api assignApi = RetrofitClient.getRetrofit().create(Api.class);
        Call<Form> assignCall = assignApi.assignUser(String.valueOf(student_id), 39);
        assignCall.enqueue(new Callback<Form>() {
            @Override
            public void onResponse(@NonNull Call<Form> call, @NonNull Response<Form> response) {

            }
            @Override
            public void onFailure(@NonNull Call<Form> call, @NonNull Throwable t) {

            }
        });


        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("Daily Form") // title for notification
                .setContentText("Please submit your daily form and tell us how you feel today.")// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent repeating_intent = new Intent(context, HostActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(100, mBuilder.build());

    }



}

