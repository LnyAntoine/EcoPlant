package com.launay.ecoplant.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.launay.ecoplant.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentToUMA = new Intent(this,UnloggedMainActivity.class);
        Intent intentToLMA = new Intent(this,LoggedMainActivity.class);
        startActivity(intentToUMA);

    }
}