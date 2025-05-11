package com.launay.ecoplant.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.launay.ecoplant.R;
import com.launay.ecoplant.activities.LoggedMainActivity;
import com.launay.ecoplant.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        Button signUpBtn = view.findViewById(R.id.signUp);
        Button toLoginBtn = view.findViewById(R.id.toLoginBtn);

        EditText fullnameField = view.findViewById(R.id.fullnameField);
        EditText mailField = view.findViewById(R.id.emailField);
        EditText displayNameField = view.findViewById(R.id.displayNameField);
        EditText pwdField = view.findViewById(R.id.pwdField);


        toLoginBtn.setOnClickListener(v->{
            Fragment loginF = new LoginFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,loginF)
                    .addToBackStack(null)
                    .commit();
        });

        signUpBtn.setOnClickListener(v->{
            String mail = mailField.getText().toString();
            String fullname = fullnameField.getText().toString();
            String pwd = pwdField.getText().toString();
            String displayName = displayNameField.getText().toString();

            //TODO quand firebase auth et BDD active activer le code :
            /*
            FirebaseAuth auth = FirebaseAuth.getInstance();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");

            auth.createUserWithEmailAndPassword(mail, pwd)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                String uid = firebaseUser.getUid();

                                // Création de l'objet utilisateur personnalisé
                                User user = new User(uid,fullname,displayName,mail,pwd);

                                // Stockage dans la base de données
                                dbRef.child(uid).setValue(user)
                                        .addOnSuccessListener(unused -> {
                                            FirebaseUser u = auth.getCurrentUser();
                                            if (u!=null){
                                                Intent toLoggedMainIntent = new Intent(requireActivity(), LoggedMainActivity.class);
                                                startActivity(toLoggedMainIntent);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            //N'arrive pas à enregistrer dans la bdd
                                        });
                            }
                        } else {
                            //N'arrive pas à s'inscrire
                        }
                    });
            */
            //TODO quand firebase auth et BDD active retirer le code :

            if (true){
                Intent toLoggedMainIntent = new Intent(requireActivity(), LoggedMainActivity.class);
                startActivity(toLoggedMainIntent);
            }

        });

        return view;
    }
}