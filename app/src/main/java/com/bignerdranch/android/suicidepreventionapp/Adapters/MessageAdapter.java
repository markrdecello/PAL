
package com.bignerdranch.android.suicidepreventionapp.Adapters;

import android.content.Context;
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

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (messages.get(position).getCounselorId() < 0) {
            viewType = R.layout.item_message_received;
        } else {
            viewType = R.layout.item_message_sent;
        }
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        final RecyclerView.ViewHolder holder;
        switch (viewType) {
            case R.layout.item_message_sent:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
                holder = new sentMessageHolder(view);
                break;
            case R.layout.item_message_received:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received, parent, false);
                holder = new receivedMessageHolder(view);
                break;

            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
                holder = new sentMessageHolder(view);
                break;

        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder instanceof sentMessageHolder) {
            ((sentMessageHolder) holder).textMessageName.setText("You");
            ((sentMessageHolder) holder).textMessageBody.setText(message.getMessageLast());
            ((sentMessageHolder) holder).textMessageTime.setText(message.getUpdatedAt());
        } else if (holder instanceof receivedMessageHolder) {
            ((receivedMessageHolder) holder).messageBody.setText(message.getMessageLast());
            ((receivedMessageHolder) holder).messageTime.setText(message.getUpdatedAt());
            ((receivedMessageHolder) holder).messageName.setText("Counselor");

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class sentMessageHolder extends RecyclerView.ViewHolder {
        private TextView textMessageBody;
        private TextView textMessageTime;
        private TextView textMessageName;


        public sentMessageHolder(View itemView) {
            super(itemView);
            textMessageName = (TextView) itemView.findViewById(R.id.text_message_name);
            textMessageBody = (TextView) itemView.findViewById(R.id.text_message_body);
           // textMessageTime = (TextView) itemView.findViewById(R.id.text_message_time);

        }
    }

    public class receivedMessageHolder extends RecyclerView.ViewHolder {
        private TextView messageName;
        private TextView messageBody;
        private TextView messageTime;

        public receivedMessageHolder(View itemView) {
            super(itemView);
            messageName = (TextView) itemView.findViewById(R.id.message_name);
            messageBody = (TextView) itemView.findViewById(R.id.message_body);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);
        }
    }
}
