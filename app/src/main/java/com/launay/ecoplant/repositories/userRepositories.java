package com.launay.ecoplant.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.launay.ecoplant.models.User;

public class userRepositories {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();


    public userRepositories(){
        User fakeuser = new User("1","Launay Antoine","LnyAntoine","launay.antoine1509@gmail.com","12345");
        this.userLiveData.setValue(fakeuser);
    }

    public LiveData<User> getUserLiveData(){
        return this.userLiveData;
    }

    public void loadCurrentUser(){
        /*
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.getIdToken(true).addOnSuccessListener(result -> {
                String idToken = result.getToken();
                String uid = firebaseUser.getUid();
                String email = firebaseUser.getEmail();

                // Exemple de transformation vers ton modèle User
                User user = new User(uid, email, idToken);
                userLiveData.postValue(user);
            }).addOnFailureListener(e -> {
                userLiveData.postValue(null);
            });
        } else {
            userLiveData.postValue(null);
        }
         */
    }

    public void logout(){
        this.userLiveData.setValue(null);
    }
}
