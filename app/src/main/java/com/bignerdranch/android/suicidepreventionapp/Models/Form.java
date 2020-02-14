
package com.bignerdranch.android.suicidepreventionapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Form {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("form_id")
    @Expose
    private Integer formId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private int answer;
    @SerializedName("name")
    @Expose
    private String name;
    private int status;


    public Form(Integer id, Integer formId, String question, int answer, String name, Integer status) {
        super();
        this.id = id;
        this.formId = formId;
        this.question = question;
        this.answer = answer;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}