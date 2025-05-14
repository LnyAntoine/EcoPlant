package com.launay.ecoplant.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.launay.ecoplant.models.User;
import com.launay.ecoplant.repositories.UserRepositories;

public class UserViewModel extends AndroidViewModel {
    private final UserRepositories userRepositories;
    private final LiveData<User> currentUser;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.userRepositories = UserRepositories.getInstance();
        this.currentUser = userRepositories.getUserLiveData();
    }
    public LiveData<User> getCurrentUser(){
        return this.currentUser;
    }
    public void loadCurrentUser(){
        this.userRepositories.loadCurrentUser();
    }
    public void signOut(){
        this.userRepositories.logout();
    }
}
