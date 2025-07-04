package com.launay.ecoplant.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.launay.ecoplant.models.entity.User;
import com.launay.ecoplant.models.repositories.UserRepositories;

import java.util.function.Consumer;

public class UserViewModel extends AndroidViewModel {
    private final UserRepositories userRepositories;
    private final LiveData<User> currentUser;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.userRepositories = UserRepositories.getInstance();
        this.currentUser = userRepositories.getUserLiveData();
    }
    public void updateUser(User user, Consumer<Boolean> callback){
            userRepositories.updateUser(user,callback);
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
