package com.launay.ecoplant.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.launay.ecoplant.R.*;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.launay.ecoplant.R;

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
    private List<Plot> plots;

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
        RecyclerView plotListRCV = view.findViewById(R.id.plot_list);

        //TODO récupérer la liste de tous les plots


        plots = new ArrayList<>();

        Plot plot1 = new Plot("1", "Jardin du Nord", 15, 1.0, 1.0, 1.0);
        Plot plot2 = new Plot("2", "Parcelle Est", 20, 1.0, 1.0, 1.0);
        Plot plot3 = new Plot("3", "Zone Sud", 12, 1.0, 1.0, 1.0);
        Plot plot4 = new Plot("4", "Coin Vert", 18, 1.0, 1.0, 1.0);
        Plot plot5 = new Plot("5", "Terre Riche", 25, 1.0, 1.0, 1.0);

        plots.add(plot1);
        plots.add(plot2);
        plots.add(plot3);
        plots.add(plot4);
        plots.add(plot5);

        PlotAdapter adapter = new PlotAdapter(requireActivity(),plots,getParentFragmentManager());
        plotListRCV.setLayoutManager(new LinearLayoutManager(requireActivity()));
        plotListRCV.setAdapter(adapter);

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
            holder.bind(plot,this);

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
                    if (plot.getPlotname().toLowerCase().contains(text)) {
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
        private final TextView azoteScore;
        private final TextView groundScore;
        private final TextView waterScore;
        //private final ImageView flagImage;


        public PlotViewHolder(@NonNull View itemView,FragmentManager fragmentManager) {
            super(itemView);
            this.fragmentManager = fragmentManager;
            this.detailedField = itemView.findViewById(R.id.detailed_field);
            this.plotName = itemView.findViewById(R.id.plot_name);
            this.nbPlant = itemView.findViewById(R.id.nb_plant);
            this.detailsBtn = itemView.findViewById(R.id.details_field);
            this.manageBtn = itemView.findViewById(R.id.manage_plot);
            this.azoteScore = itemView.findViewById(R.id.azote_score);
            this.waterScore = itemView.findViewById(R.id.water_score);
            this.groundScore = itemView.findViewById(R.id.ground_score);
        }

        public void bind(Plot plot, PlotAdapter adapter) {
            plotName.setText(plot.getPlotname());
            nbPlant.setText("Nb plant : "+plot.getNbPlant());
            azoteScore.setText(plot.getAzoteScore().toString());
            waterScore.setText(plot.getWaterScore().toString());
            groundScore.setText(plot.getGroundScore().toString());
            detailedField.setVisibility(GONE);

            azoteScore.setBackgroundColor(getResources().getColor(R.color.pale_red));
            waterScore.setBackgroundColor(getResources().getColor(R.color.pale_red));
            groundScore.setBackgroundColor(getResources().getColor(R.color.pale_red));

            if (plot.getAzoteScore()>=0.33){
                azoteScore.setBackgroundColor(getResources().getColor(color.pale_orange));
                if (plot.getAzoteScore()>=0.66){
                    azoteScore.setBackgroundColor(getResources().getColor(color.pale_green));
                }
            }
            if (plot.getGroundScore()>=0.33){
                groundScore.setBackgroundColor(getResources().getColor(color.pale_orange));
                if (plot.getGroundScore()>=0.66){
                    groundScore.setBackgroundColor(getResources().getColor(color.pale_green));
                }
            }
            if (plot.getWaterScore()>=0.33){
                waterScore.setBackgroundColor(getResources().getColor(color.pale_orange));
                if (plot.getWaterScore()>=0.66){
                    waterScore.setBackgroundColor(getResources().getColor(color.pale_green));
                }
            }



            detailsBtn.setOnClickListener(v -> {
                if (detailedField.getVisibility() == GONE){
                    detailedField.setVisibility(VISIBLE);
                } else {
                    detailedField.setVisibility(GONE);
                }
            });

            manageBtn.setOnClickListener(v->{
                String plotID;
                plotID = plot.getId();
                Fragment fragment = ManagePlotFragment.newInstance(false,plotID);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack("manage_plot_fragment")
                        .commit();
            });

        }
    }

    public static class Plot {
        private final String id;
        private final String plotname;
        private final int nbPlant;

        private final Double azoteScore;
        private final Double groundScore;
        private final Double waterScore;



        public Plot(String id, String plotname, int nbPlant, Double azoteScore, Double groundScore, Double waterScore) {
            this.plotname = plotname;
            this.id = id;
            this.nbPlant = nbPlant;
            this.azoteScore = azoteScore;
            this.groundScore = groundScore;
            this.waterScore = waterScore;

        }

        public String getId() {
            return id;
        }


        public String getPlotname() {
            return plotname;
        }

        public int getNbPlant() {
            return nbPlant;
        }

        public Double getAzoteScore() {
            return azoteScore;
        }

        public Double getGroundScore() {
            return groundScore;
        }

        public Double getWaterScore() {
            return waterScore;
        }

        @Override
        @NonNull
        public String toString(){
            return this.getPlotname();
        }
    }
}