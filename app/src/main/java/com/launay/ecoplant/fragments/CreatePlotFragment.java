package com.launay.ecoplant.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.imageview.ShapeableImageView;
import com.launay.ecoplant.R;
import com.launay.ecoplant.viewmodels.PlotViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePlotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePlotFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreatePlotFragment() {

    }

    public static CreatePlotFragment newInstance(String param1, String param2) {
        CreatePlotFragment fragment = new CreatePlotFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_plot, container, false);

        Button modifyPictureBtn = view.findViewById(R.id.change_picture_btn);
        ShapeableImageView picture = view.findViewById(R.id.imageView);
        AppCompatEditText changeName = view.findViewById(R.id.name_field);
        Button createBtn = view.findViewById(R.id.create_btn);
        Button cancelBtn  = view.findViewById(R.id.cancel_btn);

        PlotViewModel plotViewModel = new ViewModelProvider(requireActivity()).get(PlotViewModel.class);


        modifyPictureBtn.setOnClickListener(v->{
            //TODO ouvrir la gallerie et afficher l'image selectionné (voir tp1)
        });

        createBtn.setOnClickListener(v->{

            plotViewModel.createPlot(changeName.getText().toString(),plotid -> {
                if (!plotid.isEmpty()) {
                    Fragment fragment = ManagePlotFragment.newInstance(true,plotid);
                    getParentFragmentManager().beginTransaction()
                            .addToBackStack("manage_plot_fragment")
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }
            });

        });

        cancelBtn.setOnClickListener(v->{
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}