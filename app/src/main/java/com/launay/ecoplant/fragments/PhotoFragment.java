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

import com.google.android.material.imageview.ShapeableImageView;
import com.launay.ecoplant.R;
import com.launay.ecoplant.models.Plot;
import com.launay.ecoplant.viewmodels.ViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "plotID";

    // TODO: Rename and change types of parameters
    private String plotID;

    public PhotoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PhotoFragment newInstance(String param1) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plotID = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Button switchPlotBtn = view.findViewById(R.id.switch_plot_btn);
        View plantField = view.findViewById(R.id.plant_field);
        ImageButton photoBtn = view.findViewById(R.id.photo_btn);
        ImageButton galleryBtn = view.findViewById(R.id.gallery_btn);
        RecyclerView plantRCV = view.findViewById(R.id.plant_list);
        View currentPlotView = view.findViewById(R.id.current_plot);
        TextView plotName = currentPlotView.findViewById(R.id.plot_name);
        TextView plotNbPlant = currentPlotView.findViewById(R.id.nb_plant);

        ViewModel viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);


        viewModel.getCurrentPlotLiveData().observe(requireActivity(),p -> {
            if (p!=null){
                currentPlotView.setVisibility(VISIBLE);
                plotName.setText(p.getName());
                plotNbPlant.setText(p.getNbPlant()+" plantes");
            }
            else {
                currentPlotView.setVisibility(GONE);
            }

        });


        List<Plant> plants = new ArrayList<>();

        //TODO : Faire une liste de plante pour l'adapter du recycler view
        //TODO : récupérer le plot actuel


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

        plantRCV.setLayoutManager(new LinearLayoutManager(requireActivity()));
        plantRCV.setAdapter(new PlantAdapter(requireContext(),plants,getParentFragmentManager()));


        return view;
    }

    public class PlantAdapter extends RecyclerView.Adapter<PlantViewHolder> {

        private final List<Plant> plantList;
        private final Context ctx;
        private final FragmentManager fragmentManager;

        public PlantAdapter(Context ctx, List<Plant> plants, FragmentManager fragmentManager) {
            this.plantList = new ArrayList<>(plants);
            this.ctx = ctx;
            this.fragmentManager = fragmentManager;
        }

        @NonNull
        @Override
        public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.plant_item, parent, false);
            return new PlantViewHolder(view,fragmentManager);
        }

        @Override
        public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
            Plant plot = plantList.get(position);
            holder.bind(plot,this);

        }

        @Override
        public int getItemCount() {
            return plantList.size(); // Correction ici
        }
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {
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


        public PlantViewHolder(@NonNull View itemView, FragmentManager fragmentManager) {
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

        public void bind(Plant plant, PlantAdapter adapter) {
            plantName.setText(plant.getPlantName());
            nbPlant.setText(""+plant.getNbPlant());
            plantFullName.setText(plant.getPlantfullName());
            azoteScore.setText(plant.getAzoteScore().toString());
            waterScore.setText(plant.getWaterScore().toString());
            groundScore.setText(plant.getGroundScore().toString());
            detailedField.setVisibility(GONE);

            azoteScore.setBackgroundColor(getResources().getColor(R.color.pale_red));
            waterScore.setBackgroundColor(getResources().getColor(R.color.pale_red));
            groundScore.setBackgroundColor(getResources().getColor(R.color.pale_red));

            if (plant.getAzoteScore()>=0.33){
                azoteScore.setBackgroundColor(getResources().getColor(R.color.pale_orange));
                if (plant.getAzoteScore()>=0.66){
                    azoteScore.setBackgroundColor(getResources().getColor(R.color.pale_green));
                }
            }
            if (plant.getGroundScore()>=0.33){
                groundScore.setBackgroundColor(getResources().getColor(R.color.pale_orange));
                if (plant.getGroundScore()>=0.66){
                    groundScore.setBackgroundColor(getResources().getColor(R.color.pale_green));
                }
            }
            if (plant.getWaterScore()>=0.33){
                waterScore.setBackgroundColor(getResources().getColor(R.color.pale_orange));
                if (plant.getWaterScore()>=0.66){
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

    public static class Plant {
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
    }
}