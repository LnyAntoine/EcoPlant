package com.launay.ecoplant.models.entity;

public class User {
    String userId;
    String fullname;
    String displayName;
    String mail;


    String pfpURL;

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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPfpURL(String pfpURL) {
        this.pfpURL = pfpURL;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getPfpURL() {
        return pfpURL;
    }

}
