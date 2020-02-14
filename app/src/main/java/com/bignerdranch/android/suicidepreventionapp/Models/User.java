package com.bignerdranch.android.suicidepreventionapp.Models;

public class User {
    private int role;
    private String id;
    private String name;
    private String email;
    private String birth_date;
    private String school;
    private String school_id;
    private String grad_year;
    private String gender;
    private String phone_number;
    private String counselor_id;
    private String created_at;
    private String updated_at;
    private String message;
    private String status;

    public User(int role,String id,String name, String email, String birth_date, String school, String school_id, String grad_year, String gender, String phone_number, String counselor_id, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth_date = birth_date;
        this.school = school;
        this.school_id = school_id;
        this.grad_year = grad_year;
        this.gender = gender;
        this.phone_number = phone_number;
        this.counselor_id = counselor_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage(){return message;}
    public void setMessage(String message){this.message = message;}

    public String getId(){return id;}

    public void setId(String id){this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birthday) {
        this.birth_date = birth_date;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getGrad_year() {
        return grad_year;
    }

    public void setGrad_year(String grad_year) {
        this.grad_year = grad_year;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCounselor_id() {
        return counselor_id;
    }

    public void setCounselor_id(String counselor_id) {
        this.counselor_id = counselor_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}

