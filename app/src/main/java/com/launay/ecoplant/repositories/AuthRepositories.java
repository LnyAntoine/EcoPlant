package com.launay.ecoplant.repositories;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.launay.ecoplant.models.User;

import org.checkerframework.checker.units.qual.A;

public class AuthRepositories {
    private static AuthRepositories instance;
    private final MutableLiveData<FirebaseUser> currentUser;
    private final FirebaseAuth auth;
    private final DatabaseReference userDbRef;


    private AuthRepositories() {
        auth = FirebaseAuth.getInstance();
        currentUser = new MutableLiveData<>();
        currentUser.setValue(auth.getCurrentUser());
        userDbRef = FirebaseDatabase.getInstance().getReference("users");
    }
    public static synchronized AuthRepositories getInstance() {
        if (instance == null) {
            instance = new AuthRepositories();
        }
        return instance;
    }
    public void loadCurrentUser(){
        this.currentUser.setValue(auth.getCurrentUser());
    }
    public boolean signIn(String mail,String pwd){
        if (!this.isSignedIn()){
            this.auth.createUserWithEmailAndPassword(mail, pwd)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            this.currentUser.setValue(this.auth.getCurrentUser());
                        }});
            return this.isSignedIn();
        }
        return false;
    }
    public boolean signUp(String mail,String pwd, String fullname,String displayName){

            if (this.isSignedIn()){
                return false;
            }

            auth.createUserWithEmailAndPassword(mail, pwd)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            currentUser.setValue(auth.getCurrentUser());
                            if (this.isSignedIn()) {
                                String uid = currentUser.getValue().getUid();

                                User user = new User(uid,fullname,displayName,mail,pwd);

                                userDbRef.child(uid).setValue(user)
                                        .addOnSuccessListener(unused -> {

                                        })
                                        .addOnFailureListener(e -> {
                                            //N'arrive pas à enregistrer dans la bdd
                                            this.signOut();
                                        });
                            }
                        } else {
                            //N'arrive pas à s'inscrire
                        }
                    });


        return this.isSignedIn();
    }

    public boolean isSignedIn(){
        return currentUser!=null;
    }
    public MutableLiveData<FirebaseUser> getCurrentUser() {
        return this.currentUser;
    }

    public void signOut() {
        auth.signOut();
    }
}
