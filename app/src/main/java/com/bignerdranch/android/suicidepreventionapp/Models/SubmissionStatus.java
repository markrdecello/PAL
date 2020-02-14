package com.bignerdranch.android.suicidepreventionapp.Models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmissionStatus {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    public SubmissionStatus(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}