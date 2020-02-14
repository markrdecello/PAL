package com.bignerdranch.android.suicidepreventionapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.android.suicidepreventionapp.Models.Form;
import com.bignerdranch.android.suicidepreventionapp.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<Form> mForms;
    int[] answers;

    public RecyclerViewAdapter(Context context, List<Form> mForms) {
        this.context = context;
        this.mForms = mForms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answer_list_item, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Form form = mForms.get(position);
        holder.myQuestion.setText(form.getQuestion());
        holder.myQuestion.setTextColor(Color.BLACK);
        switch (form.getAnswer()){
            case -2:
                holder.myAnswer.setText("Strongly Disagree");
                break;
            case -1:
                holder.myAnswer.setText("Disagree");
                break;
            case 0:
                holder.myAnswer.setText("Neutral");
                break;
            case 1:
                holder.myAnswer.setText("Agree");
                break;
            case 2:
                holder.myAnswer.setText("Strongly Agree");
                break;
            case 3:
                holder.myAnswer.setText("I feel my sadness or pain inside is almost unbearable today.");
                break;
            case 4:
                holder.myAnswer.setText("I feel extremely down or sad today.");
                break;
            case 5:
                holder.myAnswer.setText("I feel pretty down or sad today.");
                break;
            case 6:
                holder.myAnswer.setText("I feel little bit down or sad today..");
                break;
            case 7:
                holder.myAnswer.setText("I feel nothing good or bad today.");
                break;
            case 8:
                holder.myAnswer.setText("I feel ok or fine today");
                break;
            case 9:
                holder.myAnswer.setText("I feel good or happy today.");
                break;
            case 10:
                holder.myAnswer.setText("I feel I cannot find any hope in me today.");
                break;
            case 11:
                holder.myAnswer.setText("I feel extremely hopeless today.");
                break;
            case 12:
                holder.myAnswer.setText("I feel preety hopeless today.");
                break;
            case 13:
                holder.myAnswer.setText("I feel little bit hopeless today.");
                break;
            case 14:
                holder.myAnswer.setText("I don't really feel hopeless or hopeful today.");
                break;
            case 15:
                holder.myAnswer.setText("I feel a little bit hope in me today.");
                break;
            case 16:
                holder.myAnswer.setText("I feel quite hopeful today.");
                break;
        }



        holder.myAnswer.setTextColor(Color.BLACK);


    }

    @Override
    public int getItemCount() {
        return mForms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView myQuestion;
        private TextView myAnswer;
        private RelativeLayout myParentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            myQuestion = (TextView)itemView.findViewById(R.id.my_question);
            myAnswer = (TextView)itemView.findViewById(R.id.my_answer);
            myParentLayout = (RelativeLayout)itemView.findViewById(R.id.my_parent_layout);

        }


        }

}
