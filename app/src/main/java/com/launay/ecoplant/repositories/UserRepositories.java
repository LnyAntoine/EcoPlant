package com.launay.ecoplant.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.launay.ecoplant.models.User;

public class UserRepositories {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    private static UserRepositories instance;

    private UserRepositories(){
        this.loadCurrentUser();
        //TODO quand firebase auth et BDD active retirer le code :
        User fakeuser = new User("1","Launay Antoine","LnyAntoine","launay.antoine1509@gmail.com","12345");
        this.userLiveData.setValue(fakeuser);

    }

    public static synchronized UserRepositories getInstance() {
        if (instance == null) {
            instance = new UserRepositories();
        }
        return instance;
    }

    public MutableLiveData<User> getUserLiveData(){
        return this.userLiveData;
    }

    public void loadCurrentUser(){

        //TODO quand firebase auth et BDD active activer le code :
        /*
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    userLiveData.setValue(user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Gérer les erreurs ici
                }
            });
        } else {
            userLiveData.setValue(null);
        }
        */




    }

    public void logout(){
        //TODO quand firebase auth et BDD active activer le code :
        /*
        FirebaseAuth.getInstance().signOut();
        */
        this.userLiveData.setValue(null);
    }
}
