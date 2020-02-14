package com.bignerdranch.android.suicidepreventionapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.suicidepreventionapp.Api;
import com.bignerdranch.android.suicidepreventionapp.Counselor.CounselorChatRoom;
import com.bignerdranch.android.suicidepreventionapp.Counselor.CounselorJoinChatRoom;
import com.bignerdranch.android.suicidepreventionapp.Models.KnockResponse;
import com.bignerdranch.android.suicidepreventionapp.Models.Message;
import com.bignerdranch.android.suicidepreventionapp.Models.Student;
import com.bignerdranch.android.suicidepreventionapp.Models.User;
import com.bignerdranch.android.suicidepreventionapp.R;
import com.bignerdranch.android.suicidepreventionapp.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder>{
    private Context context;
    private List<Student> mStudents;
    private int chat_id;

    public ChatAdapter(Context context, List<Student> mStudents) {
        this.context = context;
        this.mStudents = mStudents;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test, parent, false);

        return new ChatHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatHolder holder, int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("myLogin", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final Student student = mStudents.get(position);
        holder.studentName.setText(student.getName());
        holder.studentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CounselorJoinChatRoom.class);
                context.startActivity(intent);
                editor.putInt("student_id", student.getId()).apply();
                editor.putString("student_name", student.getName()).apply();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStudents.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder{
        private TextView studentName;
        public ChatHolder(View itemView) {
            super(itemView);
            studentName = (TextView)itemView.findViewById(R.id.student_name);
        }
    }
}
