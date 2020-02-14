package com.bignerdranch.android.suicidepreventionapp.Models;

public class Answer {
    private int id;
    private String answer;
    private int question_id;
    private int submitted_id;

    public Answer(int id, String answer, int question_id, int submitted_id) {
        this.id = id;
        this.answer = answer;
        this.question_id = question_id;
        this.submitted_id = submitted_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getSubmitted_id() {
        return submitted_id;
    }

    public void setSubmitted_id(int submitted_id) {
        this.submitted_id = submitted_id;
    }
}