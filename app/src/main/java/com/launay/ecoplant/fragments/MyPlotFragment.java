package com.launay.ecoplant.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.launay.ecoplant.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPlotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPlotFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyPlotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPlotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPlotFragment newInstance(String param1, String param2) {
        MyPlotFragment fragment = new MyPlotFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_plot, container, false);
        SearchView searchView = view.findViewById(R.id.search_bar);
        Button addPlotBtn = view.findViewById(R.id.add_plot);
        RecyclerView plotListRV = view.findViewById(R.id.plot_list);

        //TODO faire l'adapter du RecyclerView et récupérer la liste de tous les plots
        // Ajouter le onclickListener qui envoie sur managePlot
        /* TODO pour l'adapter du recyclerview :
        Button managePlotBtn = view.findViewById(R.id.manage_plot);
        managePlotBtn.setOnClickListener(v->{
            Fragment fragment = new CreatePlotFragment();
            Bundle bundle = new Bundle();
            bundle.putString("plot_id","randomid");
            fragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack("create_plot_fragment")
                    .commit();
        });*/

        addPlotBtn.setOnClickListener(v->{
            Fragment fragment = new ManagePlotFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack("manage_plot_fragment")
                    .commit();
        });

        //TODO gérer la searchbar


        return view;
    }
}