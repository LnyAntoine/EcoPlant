package com.launay.ecoplant.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.launay.ecoplant.R;
import com.launay.ecoplant.viewmodels.PlotViewModel;
import com.launay.ecoplant.viewmodels.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwitchPlotPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwitchPlotPhotoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlotAdapter adapter;



    public SwitchPlotPhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SwitchPlotPhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SwitchPlotPhotoFragment newInstance(String param1, String param2) {
        SwitchPlotPhotoFragment fragment = new SwitchPlotPhotoFragment();
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
        View view = inflater.inflate(R.layout.fragment_switch_plot_photo, container, false);
        Button cancelBtn = view.findViewById(R.id.cancel_btn);
        RecyclerView plotListRV = view.findViewById(R.id.plot_list);

        View currentPlotView = view.findViewById(R.id.current_plot);
        TextView plotName = currentPlotView.findViewById(R.id.plot_name);
        TextView plotNbPlant = currentPlotView.findViewById(R.id.nb_plant);

        adapter = new PlotAdapter(requireActivity(),new ArrayList<>(),getParentFragmentManager());
        plotListRV.setLayoutManager(new LinearLayoutManager(requireActivity()));
        plotListRV.setAdapter(adapter);

        PlotViewModel plotViewModel = new ViewModelProvider(requireActivity()).get(PlotViewModel.class);
        plotViewModel.loadPlots();

        plotViewModel.getCurrentPlotLiveData().observe(requireActivity(),p -> {
            if (p!=null){
                currentPlotView.setVisibility(VISIBLE);
                plotName.setText(p.getName());
                plotNbPlant.setText(p.getNbPlant()+" plantes");
            }
            else {
                currentPlotView.setVisibility(GONE);
            }

        });

        plotViewModel.getPlotsLiveData().observe(requireActivity(),plots -> {
            List<Plot> plotList = new ArrayList<>();
            plots.forEach(plot -> {

                Plot plot1 = new Plot(plot.getPlotId(),plot.getName(),plot.getNbPlant(),
                        plot.getScoreAzote(),plot.getScoreStruct(),plot.getScoreWater());
                plotList.add(plot1);

            });
            adapter.updateList(plotList);
        });


        cancelBtn.setOnClickListener(v->{
            getParentFragmentManager().popBackStack();
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

        public void updateList(List<Plot> nouvelleListe) {
            Log.d("UpdateList",""+nouvelleListe);
            this.allPlots.clear();
            this.allPlots.addAll(nouvelleListe);
            filteredPlots.clear();
            filteredPlots.addAll(nouvelleListe);
            notifyDataSetChanged(); // Peut être remplacé par DiffUtil pour plus d'efficacité
        }

        @NonNull
        @Override
        public PlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.choosing_plot_item, parent, false);
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



        private final TextView plotName;
        private final TextView nbPlant;
        private final ImageButton chooseBtn;

        //private final ImageView flagImage;


        public PlotViewHolder(@NonNull View itemView,FragmentManager fragmentManager) {
            super(itemView);
            this.fragmentManager = fragmentManager;
            this.plotName = itemView.findViewById(R.id.plot_name);
            this.nbPlant = itemView.findViewById(R.id.nb_plant);
            this.chooseBtn = itemView.findViewById(R.id.choose_btn);


        }

        public void bind(Plot plot, PlotAdapter adapter) {
            plotName.setText(plot.getPlotname());
            nbPlant.setText("Nb plant : "+plot.getNbPlant());
            chooseBtn.setOnClickListener(v->{

                PlotViewModel plotViewModel = new ViewModelProvider(requireActivity()).get(PlotViewModel.class);
                plotViewModel.loadCurrentPlot(plot.getId());
                getParentFragmentManager().popBackStack();
            });
        }
    }

    public static class Plot {
        private final String id;
        private final String plotname;
        private final int nbPlant;




        public Plot(String id, String plotname, int nbPlant, Double azoteScore, Double groundScore, Double waterScore) {
            this.plotname = plotname;
            this.id = id;
            this.nbPlant = nbPlant;

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


        @Override
        @NonNull
        public String toString(){
            return this.getPlotname();
        }
    }
}