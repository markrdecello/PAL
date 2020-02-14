package com.bignerdranch.android.suicidepreventionapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.suicidepreventionapp.Models.ChatMessage;
import com.bignerdranch.android.suicidepreventionapp.R;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.Viewholder>{
    private Context context;
    private List<ChatMessage> mChatMessages;

    public ChatMessageAdapter(Context context, List<ChatMessage> chatMessages) {
        this.context = context;
        mChatMessages = chatMessages;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_sent, parent, false);

        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ChatMessage chatMessage = mChatMessages.get(position);
        holder.textMessageName.setText(chatMessage.getSender());
        holder.textMessageBody.setText(chatMessage.getChatMessage());


    }

    @Override
    public int getItemCount() {
        return mChatMessages.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        private TextView textMessageName;
        private TextView textMessageBody;

        public Viewholder(View itemView) {
            super(itemView);
            textMessageName = itemView.findViewById(R.id.text_message_name);
            textMessageBody = itemView.findViewById(R.id.text_message_body);
        }
    }

}
