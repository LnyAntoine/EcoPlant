package com.launay.ecoplant.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.launay.ecoplant.R;
import com.launay.ecoplant.models.PlantService;
import com.launay.ecoplant.viewmodels.AuthViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //TODO : donner les autorisations des images
        //TODO : donner les autorisations internet
        //TODO : verifier la connexion à internet
        //TODO : verifier comment fonctionne la bdd plantnet
        //TODO : mettre la clé d'api plantnet sur firebase
        //TODO quand firebase auth et BDD active activer le code :

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.loadCurrentUser();




        authViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                Log.d("Firebase", "SIGNEDIN " + user.getEmail());
                Intent intentToLMA = new Intent(this, LoggedMainActivity.class);
                startActivity(intentToLMA);
            } else {
                Log.d("Firebase", "NOT SIGNEDIN");
                Intent intentToUMA = new Intent(this, UnloggedMainActivity.class);
                startActivity(intentToUMA);
            }
        });
    }
}