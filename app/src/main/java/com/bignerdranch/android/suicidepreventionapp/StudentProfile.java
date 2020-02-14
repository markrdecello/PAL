package com.bignerdranch.android.suicidepreventionapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.bignerdranch.android.suicidepreventionapp.Models.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentProfile extends Fragment {
    private TextView profileName, profileEmail, profileBirthday, profilePhoneNumber, profileGradYear, profileSchool, profileSchoolId, profileCounselorId;
    private String counselorId;
    String counselorName = "";
    private int userRole;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.student_profile, container, false);
        profileName = v.findViewById(R.id.profile_name);
        profileEmail = v.findViewById(R.id.profile_email);
        profileBirthday = v.findViewById(R.id.profile_birthday);
        profilePhoneNumber = v.findViewById(R.id.profile_phone_number);
        profileSchool = v.findViewById(R.id.profile_school);
        profileSchoolId = v.findViewById(R.id.profile_school_id);
        profileCounselorId = v.findViewById(R.id.profile_counselor_id);
        profileGradYear = v.findViewById(R.id.profile_graduation_year);
        Button editButton = v.findViewById(R.id.edit_button);

        Socket socket;
        try {
            socket = IO.socket("http://pal.njcuacm.org:443");
        }catch (URISyntaxException e){
            throw new RuntimeException(e);
        }

        socket.connect();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), Notification_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

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
                    profileGradYear.setText(user.getGrad_year());
                    userRole = user.getRole();
                    counselorId = user.getCounselor_id();
                    editor.putString("student_name", user.getName()).apply();
                    editor.putString("user_name", user.getMessage()).apply();
                    editor.putString("counselor_id", counselorId).apply();
                    editor.putInt("role", userRole).apply();

                    Api mApi = RetrofitClient.getRetrofit().create(Api.class);
                    Call<List<User>> mCall = mApi.getCounselorById(Integer.parseInt(counselorId));
                    mCall.enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                            List<User> counselors = response.body();
                            if (counselors != null) {
                                counselorName = counselors.get(0).getName();
                            }
                            editor.putString("counselor_name", counselorName).apply();

                        }

                        @Override
                        public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {

                        }
                    });



                }
            }
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        final String studentName = sharedPreferences.getString("student_name", "default");

        socket.on("user joined", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    String username = messageJson.getString("username");
                    if (username.equals(studentName + "WeeklyForm")){
                        showFormNotification("New Form", "Please submit your weekly form. Thank you.");
                    }


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
                    String receiver = messageJson.getString("receiver");
                    String sender = messageJson.getString("sender");
                    if (receiver.equals(studentName)){
                        showNotification("New Notification", "You got a new message from " + sender);
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
                (NotificationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity().getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getActivity().getApplicationContext(), ChatRoom.class);
        intent.putExtra("cName", counselorName);
        PendingIntent pi = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
    void showFormNotification(String title, String content) {
        NotificationManager mNotificationManager =
                (NotificationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity().getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getActivity().getApplicationContext(), HostActivity.class);
        intent.putExtra("cName", counselorName);
        PendingIntent pi = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        assert mNotificationManager != null;
        mNotificationManager.notify(0, mBuilder.build());
    }


}
