package com.launay.ecoplant.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.launay.ecoplant.R;
import com.launay.ecoplant.fragments.PhotoFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_layout);
        PhotoFragment photoFragment = new PhotoFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,photoFragment)
                .addToBackStack("photo_fragment")
                .commit();
    }
}