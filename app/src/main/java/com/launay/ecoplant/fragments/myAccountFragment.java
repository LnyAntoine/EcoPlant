package com.launay.ecoplant.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.launay.ecoplant.R;
import com.launay.ecoplant.activities.MainActivity;
import com.launay.ecoplant.viewmodels.AuthViewModel;
import com.launay.ecoplant.viewmodels.UserViewModel;
import com.launay.ecoplant.viewmodels.ViewModel;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link myAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    public myAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static myAccountFragment newInstance(String param1, String param2) {
        myAccountFragment fragment = new myAccountFragment();
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

        view = inflater.inflate(R.layout.fragment_my_account, container, false);
        ImageView pfp = view.findViewById(R.id.pfpField);
        ImageButton pfpEditBtn = view.findViewById(R.id.pfpEditBtn);
        TextView fullNameField = view.findViewById(R.id.fullNameField);
        ImageButton fullnameEditBtn = view.findViewById(R.id.fullNameEditBtn);
        TextView mailField = view.findViewById(R.id.emailField);
        ImageButton mailEditBtn = view.findViewById(R.id.emailEditBtn);
        TextView displayNameField = view.findViewById(R.id.displayNameField);
        ImageButton idEditBtn = view.findViewById(R.id.idEditBtn);
        TextView pwdFIeld = view.findViewById(R.id.pwdField);
        ImageButton pwdEditBtn = view.findViewById(R.id.pwdEditBtn);
        Button logoutBtn = view.findViewById(R.id.logoutBtn);


        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        AuthViewModel authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        userViewModel.getCurrentUser().observe(requireActivity(),user -> {
            fullNameField.setText(user.getFullname());
            mailField.setText(user.getMail());
            displayNameField.setText(user.getDisplayName());
            pwdFIeld.setText(user.getPwd());
        });

        logoutBtn.setOnClickListener(v->{
            authViewModel.signOut();
            Intent logoutIntent = new Intent(requireActivity(),MainActivity.class);
            startActivity(logoutIntent);
        });

        return view;
    }

}