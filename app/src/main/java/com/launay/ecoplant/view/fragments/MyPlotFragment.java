package com.launay.ecoplant.view.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.launay.ecoplant.R.*;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.launay.ecoplant.R;
import com.launay.ecoplant.models.entity.Plot;
import com.launay.ecoplant.viewmodels.PlotViewModel;

import java.util.ArrayList;
import java.util.List;

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
    private PlotAdapter adapter;
    private PlotViewModel plotViewModel;

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
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        plotViewModel.loadPlots();

        plotViewModel.getPlotsLiveData().observe(getViewLifecycleOwner(),plots -> {
            if (!plots.isEmpty()){adapter.updateList(plots);}
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_plot, container, false);
        SearchView searchView = view.findViewById(R.id.search_bar);
        Button addPlotBtn = view.findViewById(R.id.add_plot);
        RecyclerView plotListRCV = view.findViewById(R.id.plot_list);

        adapter = new PlotAdapter(requireActivity(),new ArrayList<>(),getParentFragmentManager());
        plotListRCV.setLayoutManager(new LinearLayoutManager(requireActivity()));
        plotListRCV.setAdapter(adapter);


        plotViewModel = new ViewModelProvider(requireActivity()).get(PlotViewModel.class);


        addPlotBtn.setOnClickListener(v->{
            Fragment fragment = new CreatePlotFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack("create_plot_fragment")
                    .commit();
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        return view;
    }

    public class PlotAdapter extends RecyclerView.Adapter<PlotViewHolder> {

        private final List<Plot> filteredPlots;
        private final List<Plot> allPlots;
        private final Context ctx;
        private final FragmentManager fragmentManager;

        public PlotAdapter(Context ctx, List<Plot> plots, FragmentManager fragmentManager) {
            this.allPlots = new ArrayList<>(plots);
            this.filteredPlots = new ArrayList<>(plots);
            this.ctx = ctx;
            this.fragmentManager = fragmentManager;
        }

        @NonNull
        @Override
        public PlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.plot_item_myplot, parent, false);
            return new PlotViewHolder(view,fragmentManager);
        }

        @Override
        public void onBindViewHolder(@NonNull PlotViewHolder holder, int position) {
            Plot plot = filteredPlots.get(position);
            Log.d("onBindViewHolder"," "+plot.toString());
            holder.bind(plot);

        }

        public void updateList(List<Plot> nouvelleListe) {
            Log.d("UpdateList",""+nouvelleListe);
            this.allPlots.clear();
            this.allPlots.addAll(nouvelleListe);
            filteredPlots.clear();
            filteredPlots.addAll(nouvelleListe);
            notifyDataSetChanged(); // Peut être remplacé par DiffUtil pour plus d'efficacité
        }

        @Override
        public int getItemCount() {
            return filteredPlots.size(); // Correction ici
        }
        public void filter(String text) {
            filteredPlots.clear();
            if (text.isEmpty()) {
                filteredPlots.addAll(allPlots);
            } else {
                text = text.toLowerCase();

                for (Plot plot : allPlots) {
                    if (plot.getName().toLowerCase().contains(text)) {
                        filteredPlots.add(plot);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public class PlotViewHolder extends RecyclerView.ViewHolder {
        private final FragmentManager fragmentManager;
        private final View detailsBtn;
        private final View manageBtn;
        private final View detailedField;

        private final TextView plotName;
        private final TextView nbPlant;
        private final ShapeableImageView imageView;
        private final Button mapBtn;
        private final TextView latTV;
        private final TextView longTv;


        public PlotViewHolder(@NonNull View itemView,FragmentManager fragmentManager) {
            super(itemView);
            this.fragmentManager = fragmentManager;
            this.detailedField = itemView.findViewById(R.id.detailed_field);
            this.plotName = itemView.findViewById(R.id.plot_name);
            this.nbPlant = itemView.findViewById(R.id.nb_plant);
            this.detailsBtn = itemView.findViewById(R.id.details_field);
            this.manageBtn = itemView.findViewById(R.id.manage_plot);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.mapBtn = itemView.findViewById(id.mapBtn);
            this.latTV = itemView.findViewById(id.latitudeVal);
            this.longTv = itemView.findViewById(id.longitudeVal);
        }

        public void bind(Plot plot) {
            plotName.setText(plot.getName());
            nbPlant.setText("Nb plant : "+plot.getNbPlant());
            detailedField.setVisibility(GONE);
            latTV.setText(plot.getLatitude().toString());
            longTv.setText(plot.getLongitude().toString());
            mapBtn.setOnClickListener(v->{
                if (isAdded()){
                    Fragment fragment = PlotMapFragment.newInstance(plot.getLatitude(), plot.getLongitude());
                    getParentFragmentManager().beginTransaction()
                            .replace(id.fragment_container, fragment)
                            .addToBackStack("PlotMapFragment")
                            .commit();
                }
            });

            detailsBtn.setOnClickListener(v -> {
                if (detailedField.getVisibility() == GONE){
                    detailedField.setVisibility(VISIBLE);
                } else {
                    detailedField.setVisibility(GONE);
                }
            });

            manageBtn.setOnClickListener(v->{
                String plotID;
                plotID = plot.getPlotId();
                Fragment fragment = ManagePlotFragment.newInstance(false,plotID);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack("manage_plot_fragment")
                        .commit();
            });

            Uri imageUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + drawable.jardin);

            String pictureUrl = plot.getPictureUrl()!=null
                    ?(plot.getPictureUrl().isEmpty()
                        ? imageUri.toString()
                        :plot.getPictureUrl()
                    )
                    :imageUri.toString();

            Glide.with(requireContext())
                    .load(pictureUrl)
                    .fitCenter()
                    .into(imageView);

        }
    }

}