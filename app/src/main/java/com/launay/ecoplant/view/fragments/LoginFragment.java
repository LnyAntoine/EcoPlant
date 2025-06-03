package com.launay.ecoplant.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.launay.ecoplant.R;
import com.launay.ecoplant.view.activities.LoggedMainActivity;
import com.launay.ecoplant.viewmodels.AuthViewModel;
import com.launay.ecoplant.viewmodels.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button loginBtn = view.findViewById(R.id.loginBtn);
        Button signupBtn = view.findViewById(R.id.toSignUpBtn);
        TextView pwdForgotten = view.findViewById(R.id.pwdForgottenLink);
        EditText mailField = view.findViewById(R.id.mailField);
        EditText pwdField = view.findViewById(R.id.pwdField);

        AuthViewModel authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        authViewModel.getCurrentUser().observe(requireActivity(),firebaseUser -> {
            //TODO retirer le ou après
            if (firebaseUser!=null){
                userViewModel.loadCurrentUser();
                Intent toLoggedMainIntent = new Intent(requireActivity(), LoggedMainActivity.class);
                startActivity(toLoggedMainIntent);
            }
        });


        pwdForgotten.setOnClickListener(v->{
            //TODO gérer le mot de passe
        });

        signupBtn.setOnClickListener(v->{
            Fragment signupF = new SignUpFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,signupF)
                    .addToBackStack(null)
                    .commit();
        });
        loginBtn.setOnClickListener(v->{
            String mail = mailField.getText().toString();
            String pwd = pwdField.getText().toString();

            authViewModel.signIn(mail,pwd,success -> {
                if (success) {

                } else {

                }
            });
            authViewModel.loadCurrentUser();
        });
        return view;
    }
}