package com.launay.ecoplant.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.launay.ecoplant.viewmodels.AuthViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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