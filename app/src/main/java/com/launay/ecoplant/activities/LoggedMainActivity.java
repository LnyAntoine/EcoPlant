package com.launay.ecoplant.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.launay.ecoplant.R;
import com.launay.ecoplant.fragments.CommunityFragment;
import com.launay.ecoplant.fragments.MapFragment;
import com.launay.ecoplant.fragments.MyPlotFragment;
import com.launay.ecoplant.fragments.PhotoFragment;
import com.launay.ecoplant.fragments.myAccountFragment;
import com.launay.ecoplant.viewmodels.AuthViewModel;
import com.launay.ecoplant.viewmodels.UserViewModel;
import com.launay.ecoplant.viewmodels.ViewModel;

public class LoggedMainActivity extends AppCompatActivity {

    Integer fragment_containerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.main_layout);
/*
        ViewModel viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.refreshUser();


 */
        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.loadCurrentUser();
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.loadCurrentUser();

        PhotoFragment photoFragment = new PhotoFragment();

        BottomNavigationView navbar = findViewById(R.id.bottom_nav);
        fragment_containerId = R.id.fragment_container;

        changeFragment(photoFragment,"photo_fragment");

        navbar.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =item.getItemId();
                if (id==R.id.accountFragment){
                    changeFragment(new myAccountFragment(),"account_fragment");
                    return true;
                }
                else {
                    if (id == R.id.photoFragment){
                        changeFragment(new PhotoFragment(),"photo_fragment");
                        return true;
                    }
                    else {
                        if (id == R.id.mapFragment){
                            changeFragment(new MapFragment(),"map_fragment");
                            return true;
                        } else {
                            if (id == R.id.communityFragment){
                                changeFragment(new CommunityFragment(),"community_fragment");
                                return true;
                            } else {
                                if (id == R.id.plotFragment){
                                    changeFragment(new MyPlotFragment(),"plot_fragment");
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        }
                    }

                }
            }
        });
    }
    void changeFragment(Fragment fragment,String name){
        getSupportFragmentManager().beginTransaction()
                .replace(fragment_containerId,fragment)
                .addToBackStack(name)
                .commit();
    }
}