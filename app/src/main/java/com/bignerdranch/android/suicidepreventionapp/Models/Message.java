package com.bignerdranch.android.suicidepreventionapp.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("student_id")
    @Expose
    private Integer studentId;
    @SerializedName("counselor_id")
    @Expose
    private Integer counselorId;
    @SerializedName("message_last")
    @Expose
    private String messageLast;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    private String student_name;

    public Message(Integer id, Integer studentId, Integer counselorId, String messageLast, String updatedAt, String createdAt, String student_name) {
        super();
        this.id = id;
        this.studentId = studentId;
        this.counselorId = counselorId;
        this.messageLast = messageLast;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.student_name = student_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(Integer counselorId) {
        this.counselorId = counselorId;
    }

    public String getMessageLast() {
        return messageLast;
    }

    public void setMessageLast(String messageLast) {
        this.messageLast = messageLast;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStudent_name() {
        return student_name;
    }
    public void setStudent_name(String student_name){
        this.student_name = student_name;

    }
}