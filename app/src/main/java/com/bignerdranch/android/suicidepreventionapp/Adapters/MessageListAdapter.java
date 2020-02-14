package com.bignerdranch.android.suicidepreventionapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.suicidepreventionapp.Models.Message;
import com.bignerdranch.android.suicidepreventionapp.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Message> messages;
    private SharedPreferences sharedPreferences;

    public MessageListAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (messages.get(position).getCounselorId() > 0){
            viewType = R.layout.counselor_received_message;
        }
        else {
            viewType = R.layout.counselor_sent_message;
        }
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        final RecyclerView.ViewHolder holder;
        switch (viewType){
            case R.layout.counselor_sent_message:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.counselor_sent_message, parent, false);
                holder = new MessageListAdapter.sentMessageHolder(view);
                break;
            case R.layout.counselor_received_message:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.counselor_received_message, parent, false);
                holder = new MessageListAdapter.receivedMessageHolder(view);
                break;

            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.counselor_sent_message, parent, false);
                holder = new MessageListAdapter.sentMessageHolder(view);
                break;

        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        sharedPreferences = context.getSharedPreferences("myLogin", Context.MODE_PRIVATE);
        if (holder instanceof MessageListAdapter.sentMessageHolder){
            ((sentMessageHolder)holder).textMessageBody.setText(message.getMessageLast());
            ((sentMessageHolder)holder).textMessageTime.setText(message.getUpdatedAt());
        }
        else if (holder instanceof MessageListAdapter.receivedMessageHolder){
            ((receivedMessageHolder)holder).messageBody.setText(message.getMessageLast());
            ((receivedMessageHolder)holder).messageTime.setText(message.getUpdatedAt());
            ((receivedMessageHolder)holder).messageName.setText("Student");




        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class sentMessageHolder extends RecyclerView.ViewHolder{
        private TextView textMessageBody;
        private TextView textMessageTime;

        public sentMessageHolder(View itemView) {
            super(itemView);
            textMessageBody = (TextView)itemView.findViewById(R.id.text_counselor_body);
            textMessageTime = (TextView)itemView.findViewById(R.id.text_counselor_time);
        }
    }
    public class receivedMessageHolder extends RecyclerView.ViewHolder{
        private TextView messageName;
        private TextView messageBody;
        private TextView messageTime;

        public receivedMessageHolder(View itemView) {
            super(itemView);
            messageName = (TextView)itemView.findViewById(R.id.counselor_message_name);
            messageBody = (TextView)itemView.findViewById(R.id.counselor_message_body);
            messageTime = (TextView)itemView.findViewById(R.id.counselor_message_time);
        }
    }
}


/*
package com.bignerdranch.android.suicidepreventionapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.suicidepreventionapp.Models.Message;
import com.bignerdranch.android.suicidepreventionapp.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Message> messages;
    private SharedPreferences sharedPreferences;

    public MessageListAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (messages.get(position).getCounselorId() > 0){
            viewType = R.layout.counselor_received_message;
        }
        else {
            viewType = R.layout.counselor_sent_message;
        }
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        final RecyclerView.ViewHolder holder;
        switch (viewType){
            case R.layout.counselor_sent_message:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.counselor_sent_message, parent, false);
                holder = new MessageListAdapter.sentMessageHolder(view);
                break;
            case R.layout.counselor_received_message:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.counselor_received_message, parent, false);
                holder = new MessageListAdapter.receivedMessageHolder(view);
                break;

            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.counselor_sent_message, parent, false);
                holder = new MessageListAdapter.sentMessageHolder(view);
                break;

        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        sharedPreferences = context.getSharedPreferences("myLogin", Context.MODE_PRIVATE);
        if (holder instanceof MessageListAdapter.sentMessageHolder){
            ((sentMessageHolder)holder).textMessageBody.setText(message.getMessageLast());
            ((sentMessageHolder)holder).textMessageTime.setText(message.getUpdatedAt());
        }
        else if (holder instanceof MessageListAdapter.receivedMessageHolder){
            ((receivedMessageHolder)holder).messageBody.setText(message.getMessageLast());
            ((receivedMessageHolder)holder).messageTime.setText(message.getUpdatedAt());
            ((receivedMessageHolder)holder).messageName.setText("Student");




        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class sentMessageHolder extends RecyclerView.ViewHolder{
        private TextView textMessageBody;
        private TextView textMessageTime;

        public sentMessageHolder(View itemView) {
            super(itemView);
            textMessageBody = (TextView)itemView.findViewById(R.id.text_counselor_body);
            textMessageTime = (TextView)itemView.findViewById(R.id.text_counselor_time);
        }
    }
    public class receivedMessageHolder extends RecyclerView.ViewHolder{
        private TextView messageName;
        private TextView messageBody;
        private TextView messageTime;

        public receivedMessageHolder(View itemView) {
            super(itemView);
            messageName = (TextView)itemView.findViewById(R.id.counselor_message_name);
            messageBody = (TextView)itemView.findViewById(R.id.counselor_message_body);
            messageTime = (TextView)itemView.findViewById(R.id.counselor_message_time);
        }
    }
}
 */