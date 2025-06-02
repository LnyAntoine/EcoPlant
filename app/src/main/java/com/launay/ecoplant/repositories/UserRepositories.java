package com.launay.ecoplant.repositories;

import android.util.Log;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.launay.ecoplant.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UserRepositories {
    private final MutableLiveData<User> userLiveData;

    private static UserRepositories instance;

    private UserRepositories(){
        userLiveData = new MutableLiveData<>();
        this.loadCurrentUser();

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

    public void updateUser(User user, Consumer<Boolean> callback) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> updates = new HashMap<>();
            updates.put("fullname",user.getFullname());
            updates.put("displayName",user.getDisplayName());
            updates.put("pfpURL",user.getPfpURL());
            db.collection("users").document(uid)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "User mise à jour avec succès"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Erreur mise à jour user", e));
        }
    }

    public void loadCurrentUser(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            user.setUserId(documentSnapshot.getId());
                            userLiveData.setValue(user);
                        } else {
                            Log.d("UserRepo","Document not found");
                            userLiveData.setValue(null); // Pas de document trouvé
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.d("UserRepo","AddOnFailureListener"+e.getMessage());
                        userLiveData.setValue(null);
                    });
        } else {
            Log.d("UserRepo","FirebaseUser null"+firebaseUser);

            userLiveData.setValue(null);
        }
    }

    public void logout(){
        instance = null;
    }
}
