package com.launay.ecoplant.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.launay.ecoplant.R;
import com.launay.ecoplant.activities.LoggedMainActivity;

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
        EditText idField = view.findViewById(R.id.idField);
        EditText pwdField = view.findViewById(R.id.pwdField);

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
            String id = idField.getText().toString();
            String pwd = pwdField.getText().toString();
            if (true){
                //Actualiser bdd
                Intent toMainIntent = new Intent(requireActivity(), LoggedMainActivity.class);
                startActivity(toMainIntent);
            }
            else {
                //gérer erreurs
            }
        });
        return view;
    }
}