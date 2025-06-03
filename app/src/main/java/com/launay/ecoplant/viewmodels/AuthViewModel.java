package com.launay.ecoplant.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;
import com.launay.ecoplant.models.repositories.AuthRepositories;
import com.launay.ecoplant.utils;

public class AuthViewModel extends AndroidViewModel {
    private final LiveData<FirebaseUser> currentUser;
    private final AuthRepositories authRepositories;
    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepositories = AuthRepositories.getInstance();
        this.currentUser = authRepositories.getCurrentUser();

    }
    public LiveData<FirebaseUser> getCurrentUser(){
        return this.currentUser;
    }
    public void loadCurrentUser(){
        this.authRepositories.loadCurrentUser();
    }
    public boolean isSignedIn(){
        return this.authRepositories.isSignedIn();
    }
    public void signOut(){
        this.authRepositories.signOut();
    }
    public void signIn(String mail, String pwd, utils.AuthCallback callback) {
        authRepositories.signIn(mail, pwd, callback);
    }

    public void signUp(String mail, String pwd, String fullname, String displayName, utils.AuthCallback callback) {
        authRepositories.signUp(mail, pwd, fullname, displayName, callback);
    }
}
