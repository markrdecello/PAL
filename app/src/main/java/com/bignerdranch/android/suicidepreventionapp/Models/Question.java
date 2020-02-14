package com.bignerdranch.android.suicidepreventionapp.Models;

public class Question {
    private int id;
    private String question;
    private int form_id;
    private int status;
    private String message;

    public Question(int id, String question, int form_id, int status, String message) {
        this.id = id;
        this.question = question;
        this.form_id = form_id;
        this.status = status;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getForm_id() {
        return form_id;
    }

    public void setForm_id(int form_id) {
        this.form_id = form_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
