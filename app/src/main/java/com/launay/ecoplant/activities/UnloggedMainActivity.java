package com.launay.ecoplant.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.launay.ecoplant.R;
import com.launay.ecoplant.fragments.SignUpFragment;

public class UnloggedMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        SignUpFragment signUpFragment = new SignUpFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,signUpFragment)
                .addToBackStack("signup_fragment")
                .commit();
    }
}