package com.launay.ecoplant.models;

public class User {
    String userId;
    String fullname;
    String displayName;
    String mail;
    String pwd;
    String pfpURI;

    public User(){}

    public User(String userId, String fullname, String displayName, String mail) {
        this.userId = userId;
        this.fullname = fullname;
        this.displayName = displayName;
        this.mail = mail;
    }

    public String getUserId() {
        return userId;
    }

    public String getFullname() {
        return fullname;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getMail() {
        return mail;
    }

    public String getPwd() {
        return pwd;
    }
}
