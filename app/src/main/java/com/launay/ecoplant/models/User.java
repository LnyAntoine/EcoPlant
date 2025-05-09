package com.launay.ecoplant.models;

public class User {
    String userId;
    String fullname;
    String userPseudo;
    String mail;
    String pwd;
    String pfpURI;


    public User(String userId, String fullname, String userPseudo, String mail, String pwd) {
        this.userId = userId;
        this.fullname = fullname;
        this.userPseudo = userPseudo;
        this.mail = mail;
        this.pwd = pwd;
    }

    public String getUserId() {
        return userId;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUserPseudo() {
        return userPseudo;
    }

    public String getMail() {
        return mail;
    }

    public String getPwd() {
        return pwd;
    }
}
