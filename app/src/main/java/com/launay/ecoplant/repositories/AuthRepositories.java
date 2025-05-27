package com.launay.ecoplant.repositories;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.launay.ecoplant.models.User;
import com.launay.ecoplant.utils;

import org.checkerframework.checker.units.qual.A;

public class AuthRepositories {
    private static AuthRepositories instance;
    private final MutableLiveData<FirebaseUser> currentUser;
    private final FirebaseAuth auth;
    //private final DatabaseReference userDbRef;


    private AuthRepositories() {
        auth = FirebaseAuth.getInstance();
        currentUser = new MutableLiveData<>();
        currentUser.setValue(auth.getCurrentUser());
        //userDbRef = FirebaseDatabase.getInstance().getReference("users");
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


    public void signIn(String mail, String pwd, utils.AuthCallback callback){
        if (this.isSignedIn()) {
            callback.onComplete(false); // déjà connecté
            return;
        }

        auth.signInWithEmailAndPassword(mail, pwd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser.setValue(auth.getCurrentUser());
                        callback.onComplete(true);
                    } else {
                        callback.onComplete(false);
                    }
                });
    }

    public void signUp(String mail, String pwd, String fullname, String displayName, utils.AuthCallback  callback) {
        if (this.isSignedIn()) {
            Log.d("SignUpFirebase","UserAlreadySignedIn"+this.isSignedIn()+this.currentUser.getValue());
            callback.onComplete(false);
            return;
        }
        Log.d("SignUpFirebase","UserNotSignedIn"+this.isSignedIn()+this.currentUser.getValue());
        auth.createUserWithEmailAndPassword(mail, pwd)
                .addOnCompleteListener(task -> {
                    Log.d("SignUpFirebase","addOnCompleteListener"+task);
                    if (task.isSuccessful()) {
                        Log.d("SignUpFirebase","TaskSuccessful"+task);
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        currentUser.setValue(firebaseUser);

                        if (firebaseUser != null) {
                            Log.d("SignUpFirebase","firebaseUser!=null"+firebaseUser);

                            String uid = firebaseUser.getUid();
                            User user = new User(uid, fullname, displayName, mail, pwd);
                            /*
                            userDbRef.child(uid).setValue(user)
                                    .addOnSuccessListener(unused -> callback.onComplete(true))
                                    .addOnFailureListener(e -> {
                                        this.signOut();
                                        callback.onComplete(false);
                                    });

                             */
                        } else {
                            Log.d("SignUpFirebase","firebaseUser==null"+firebaseUser);
                            callback.onComplete(false);
                        }
                    } else {
                        Log.d("SignUpFirebase","TaskError"+task);

                        callback.onComplete(false);
                    }
                });
    }

    public boolean isSignedIn(){
        return this.currentUser.getValue() != null;
    }
    public MutableLiveData<FirebaseUser> getCurrentUser() {
        return this.currentUser;
    }

    public void signOut() {
        Log.d("AuthRepo","SignedOut"+getCurrentUser().getValue());
        auth.signOut();
    }
}
