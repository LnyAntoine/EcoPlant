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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.launay.ecoplant.R;
import com.launay.ecoplant.models.Observation;
import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.models.Plot;
import com.launay.ecoplant.viewmodels.ObservationViewModel;
import com.launay.ecoplant.viewmodels.PlotViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManagePlotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagePlotFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "fromCreate";
    private static final String ARG_PARAM2 = "plotID";

    // TODO: Rename and change types of parameters
    private Boolean fromCreate;
    private String plotID;
    private ObservationAdapter adapter;



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
    public static ManagePlotFragment newInstance(boolean param1, String param2) {
        ManagePlotFragment fragment = new ManagePlotFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromCreate = getArguments().getBoolean(ARG_PARAM1);
            plotID = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_plot, container, false);



        Button changePictureBtn = view.findViewById(R.id.change_picture_btn);
        ImageButton editNameBtn = view.findViewById(R.id.edit_name_btn);
        EditText nameField = view.findViewById(R.id.name_field);
        Button deleteBtn = view.findViewById(R.id.delete_btn);
        ImageButton returnBtn = view.findViewById(R.id.return_btn);
        Button addPlant = view.findViewById(R.id.add_plant_btn);
        RecyclerView plantListRCV = view.findViewById(R.id.plant_list);
        TextView azoteScoreTV = view.findViewById(R.id.azote_score);
        TextView groundScoreTV = view.findViewById(R.id.ground_score);
        TextView waterScoreTV = view.findViewById(R.id.water_score);

        plantListRCV.setLayoutManager(new LinearLayoutManager(requireActivity()));

        adapter = new ObservationAdapter(requireContext(),new ArrayList<>(),getParentFragmentManager());

        plantListRCV.setAdapter(adapter);

        PlotViewModel plotViewModel = new ViewModelProvider(requireActivity()).get(PlotViewModel.class);
        //PlantInPlotViewModel plantInPlotViewModel = new ViewModelProvider(requireActivity()).get(PlantInPlotViewModel.class);
        plotViewModel.loadPlotByid(plotID);

        ObservationViewModel observationViewModel = new ViewModelProvider(requireActivity()).get(ObservationViewModel.class);

        plotViewModel.getPlotById().observe(requireActivity(),plot ->{
            if (plot!=null) {
                nameField.setText(plot.getName());
                azoteScoreTV.setText(plot.getScoreAzote().toString());
                groundScoreTV.setText(plot.getScoreStruct().toString());
                waterScoreTV.setText(plot.getScoreWater().toString());


                //TODO semble y avoir une erreur ici ?
                if (plot.getScoreAzote()>=0.33){
                    azoteScoreTV.setBackgroundColor(getResources().getColor(R.color.pale_orange));
                    if (plot.getScoreAzote()>=0.66){
                        azoteScoreTV.setBackgroundColor(getResources().getColor(R.color.pale_green));
                    }
                }
                if (plot.getScoreStruct()>=0.33){
                    groundScoreTV.setBackgroundColor(getResources().getColor(R.color.pale_orange));
                    if (plot.getScoreStruct()>=0.66){
                        groundScoreTV.setBackgroundColor(getResources().getColor(R.color.pale_green));
                    }
                }
                if (plot.getScoreWater()>=0.33){
                    waterScoreTV.setBackgroundColor(getResources().getColor(R.color.pale_orange));
                    if (plot.getScoreWater()>=0.66){
                        waterScoreTV.setBackgroundColor(getResources().getColor(R.color.pale_green));
                    }
                }


                observationViewModel.loadObservationListLiveDataByPlotId(plot.getPlotId());
                //plantInPlotViewModel.loadPlotPlants(plot.getPlotId());

            }
        });


        observationViewModel.getObservationListLiveData().observe(requireActivity(),observationList -> {
            if (observationList != null){
                if (!observationList.isEmpty()){
                    List<Observation> observations = observationViewModel.getObservationListLiveData().getValue();
                    if (observations!=null){
                        if (!observations.isEmpty()){
                            adapter.updateList(observations);
                        }
                    }
                }
            }
        });

        /*
        plantInPlotViewModel.getPlantlistLiveData().observe(requireActivity(),plantList -> {
            if (plantList != null){
                if (!plantList.isEmpty()){
                    List<PlantInPlot> plantInPlots = plantInPlotViewModel.getPlantInPlotListLiveData().getValue();
                    if (plantInPlots!=null){
                        if (!plantInPlots.isEmpty()){

                            Map<String,Integer> plantMap = new HashMap<>();
                            for (PlantInPlot pip : plantInPlots){
                                if (plantMap.containsKey(pip.getPlantId())){
                                    plantMap.replace(pip.getPlantId(),plantMap.get(pip.getPlantId())+1);
                                } else{
                                    plantMap.put(pip.getPlantId(),1);
                                }
                            }
                            List<Plant> plants = new ArrayList<>();
                            for (com.launay.ecoplant.models.Plant plant : plantList){

                                int nb = plantMap.get(plant.getPlantId());

                                Plant plant1 = new Plant(plant.getPlantId(), plant.getShortname(), nb, plant.getFullname(),
                                        plant.getScoreAzote(), plant.getScoreStruct(), plant.getScoreWater(), "");

                                plants.add(plant1);
                            }
                            adapter.updateList(plants);
                        }
                    }
                }
            }
        });
         */



        //TODO récupérer la liste des plantes pour ce plot
        List<Plant> plants = new ArrayList<>();


        nameField.setActivated(false);

        addPlant.setOnClickListener(v->{
            //TODO récupérer le plot actuel, view model ?
            Fragment fragment = new PhotoFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack("photo_fragment")
                    .commit();
        });

        editNameBtn.setOnClickListener(v->{
            nameField.setActivated(true);
        });

        returnBtn.setOnClickListener(v->{
            //TODO verifier si vient d'un endroit en particulier et renvoyer sur la liste si c'est le cas
            getParentFragmentManager().popBackStack();
        });

        deleteBtn.setOnClickListener(v->{
            Plot plot = plotViewModel.getPlotById().getValue();
            if (plot != null){
                plotViewModel.deletePlot(plot.getPlotId(),success -> {
                    if (success){

                    }
                });
            }
            getParentFragmentManager().popBackStack();
        });

        changePictureBtn.setOnClickListener(v->{
            //TODO récupérer l'image dans la gallerie
        });
        return view;
    }

    public class ObservationAdapter extends RecyclerView.Adapter<ObservationViewHolder> {

        private final List<Observation> observations;
        private final Context ctx;
        private final FragmentManager fragmentManager;

        public ObservationAdapter(Context ctx, List<Observation> observations, FragmentManager fragmentManager) {
            this.observations = new ArrayList<>(observations);
            this.ctx = ctx;
            this.fragmentManager = fragmentManager;
        }

        public void updateList(List<Observation> observationList) {
            Log.d("UpdateList",""+observationList);
            this.observations.clear();
            this.observations.addAll(observationList);
            notifyDataSetChanged(); // Peut être remplacé par DiffUtil pour plus d'efficacité
        }

        @NonNull
        @Override
        public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.plant_item, parent, false);
            return new ObservationViewHolder(view,fragmentManager);
        }

        @Override
        public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
            Observation obs = observations.get(position);
            holder.bind(obs,this);

        }

        @Override
        public int getItemCount() {
            return observations.size(); // Correction ici
        }
    }

    public class ObservationViewHolder extends RecyclerView.ViewHolder {
        private final FragmentManager fragmentManager;
        private final View detailsBtn;
        private final View detailedField;

        private final TextView plantName;
        private final TextView nbPlant;
        private final TextView azoteScore;
        private final TextView groundScore;
        private final TextView waterScore;
        private final TextView plantFullName;
        private final Button knowmoreBtn;
        private final Button addBtn;
        private final ShapeableImageView picture;
        //private final ImageView flagImage;


        public ObservationViewHolder(@NonNull View itemView, FragmentManager fragmentManager) {
            super(itemView);

            this.fragmentManager = fragmentManager;
            this.plantFullName = itemView.findViewById(R.id.full_name_value);
            this.detailedField = itemView.findViewById(R.id.detailed_field);
            this.plantName = itemView.findViewById(R.id.name_field);
            this.nbPlant = itemView.findViewById(R.id.nb_value);
            this.detailsBtn = itemView.findViewById(R.id.details_field);
            this.azoteScore = itemView.findViewById(R.id.azote_score);
            this.waterScore = itemView.findViewById(R.id.water_score);
            this.groundScore = itemView.findViewById(R.id.ground_score);
            this.knowmoreBtn =itemView.findViewById(R.id.knowmore_btn);
            this.addBtn = itemView.findViewById(R.id.add_btn);
            this.picture = itemView.findViewById(R.id.imageView);
        }

        public void bind(Observation obs, ObservationAdapter adapter) {
            Plant plant = obs.getPlant();
            plantName.setText(plant.getShortname());
            //TODO régler ça : nbPlant.setText(""+plant.getNbPlant());
            plantFullName.setText(plant.getFullname());
            azoteScore.setText(plant.getScoreAzote().toString());
            waterScore.setText(plant.getScoreWater().toString());
            groundScore.setText(plant.getScoreStruct().toString());
            detailedField.setVisibility(GONE);

            azoteScore.setBackgroundColor(getResources().getColor(R.color.pale_red));
            waterScore.setBackgroundColor(getResources().getColor(R.color.pale_red));
            groundScore.setBackgroundColor(getResources().getColor(R.color.pale_red));

            if (plant.getScoreAzote()>=0.33){
                azoteScore.setBackgroundColor(getResources().getColor(R.color.pale_orange));
                if (plant.getScoreAzote()>=0.66){
                    azoteScore.setBackgroundColor(getResources().getColor(R.color.pale_green));
                }
            }
            if (plant.getScoreStruct()>=0.33){
                groundScore.setBackgroundColor(getResources().getColor(R.color.pale_orange));
                if (plant.getScoreStruct()>=0.66){
                    groundScore.setBackgroundColor(getResources().getColor(R.color.pale_green));
                }
            }
            if (plant.getScoreWater()>=0.33){
                waterScore.setBackgroundColor(getResources().getColor(R.color.pale_orange));
                if (plant.getScoreWater()>=0.66){
                    waterScore.setBackgroundColor(getResources().getColor(R.color.pale_green));
                }
            }



            detailsBtn.setOnClickListener(v -> {
                if (detailedField.getVisibility() == GONE){
                    detailedField.setVisibility(VISIBLE);
                } else {
                    detailedField.setVisibility(GONE);
                }
            });

            addBtn.setOnClickListener(v->{
                //TODO ajouter la fleur dans la bdd
            });
            knowmoreBtn.setOnClickListener(v->{
                //TODO renvoyer vers une page web
            });

        }
    }

    /*public static class Plant {
        private final String id;
        private final String plantName;
        private final int nbPlant;

        private final String plantfullName;
        private final Double azoteScore;
        private final Double groundScore;
        private final Double waterScore;
        private final String pictureURI;



        public Plant(String id, String plantName, int nbPlant, String plantfullName, Double azoteScore, Double groundScore, Double waterScore, String pictureURI) {
            this.plantName = plantName;
            this.id = id;
            this.nbPlant = nbPlant;
            this.plantfullName = plantfullName;
            this.azoteScore = azoteScore;
            this.groundScore = groundScore;
            this.waterScore = waterScore;

            this.pictureURI = pictureURI;
        }

        public String getId() {
            return id;
        }


        public String getPlantName() {
            return plantName;
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
            return this.getPlantName();
        }

        public String getPlantfullName() {
            return plantfullName;
        }

        public String getPictureURI() {
            return pictureURI;
        }
    }*/
}