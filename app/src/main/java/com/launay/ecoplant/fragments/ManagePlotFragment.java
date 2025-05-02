package com.launay.ecoplant.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.launay.ecoplant.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManagePlotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagePlotFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManagePlotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManagePlotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManagePlotFragment newInstance(String param1, String param2) {
        ManagePlotFragment fragment = new ManagePlotFragment();
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
        View view = inflater.inflate(R.layout.fragment_manage_plot, container, false);
        Button changePictureBtn = view.findViewById(R.id.change_picture_btn);
        Button editNameBtn = view.findViewById(R.id.edit_name_btn);
        EditText nameField = view.findViewById(R.id.name_field);
        Button deleteBtn = view.findViewById(R.id.delete_btn);
        Button returnBtn = view.findViewById(R.id.return_btn);
        Button addPlant = view.findViewById(R.id.add_plant_btn);

        nameField.setActivated(false);

        addPlant.setOnClickListener(v->{
            //TODO récupérer le plot actuel, view model ?
            Fragment fragment = new PhotoFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack("photo_fragment")
                    .commit();
        });

        editNameBtn.setOnClickListener(v->{
            nameField.setActivated(true);
        });

        returnBtn.setOnClickListener(v->{
            getParentFragmentManager().popBackStack();
        });

        deleteBtn.setOnClickListener(v->{
            //TODO vérifier que l'utilisateur soit d'accord
            // supprimer le plot
            getParentFragmentManager().popBackStack();
        });

        changePictureBtn.setOnClickListener(v->{
            //TODO récupérer l'image dans la gallerie
        });
        return view;
    }
}