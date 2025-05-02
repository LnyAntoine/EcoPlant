package com.launay.ecoplant.fragments;

import static android.view.View.GONE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.launay.ecoplant.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoFragment newInstance(String param1, String param2) {
        PhotoFragment fragment = new PhotoFragment();
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
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Button switchPlotBtn = view.findViewById(R.id.switch_plot_btn);
        View plantField = view.findViewById(R.id.plant_field);
        Button photoBtn = view.findViewById(R.id.photo_btn);
        Button galleryBtn = view.findViewById(R.id.gallery_btn);

        //TODO : Faire une liste de plante pour l'adapter du recycler view
        //TODO : récupérer le plot actuel

        //TODO Si pas de plante détectée, désafficher plantfield
        //plantField.setVisibility(GONE);

        photoBtn.setOnClickListener(v->{
            //TODO Ouvrir l'appareil photo et récupérer la photo prise
        });

        galleryBtn.setOnClickListener(v->{
            //TODO Ouvrir la galerie et récupérer la photo choisie
        });

        switchPlotBtn.setOnClickListener(v->{
            Fragment fragment = new SwitchPlotPhotoFragment();
            //TODO ajouter le plot actuel dans le bundle (flemme), passer par viewmodel ?
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack("switch_plot_fragment")
                    .commit();
        });

        return view;
    }
}