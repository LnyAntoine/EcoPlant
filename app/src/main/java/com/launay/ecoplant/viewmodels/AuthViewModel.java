package com.launay.ecoplant.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.launay.ecoplant.repositories.AuthRepositories;

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
        return this.currentUser!=null;
    }
    public void signOut(){
        this.authRepositories.signOut();
    }
    public boolean signIn(String mail, String pwd){
        return authRepositories.signIn(mail, pwd);
    }
    public boolean signUp(String mail,String pwd,String fullname,String displayName){
        return authRepositories.signUp(mail,pwd,fullname,displayName);
    }
}
