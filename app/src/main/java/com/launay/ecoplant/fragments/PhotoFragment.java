package com.launay.ecoplant.fragments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.launay.ecoplant.R;
import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.models.Plot;
import com.launay.ecoplant.viewmodels.DetectedPlantViewModel;
import com.launay.ecoplant.viewmodels.ObservationViewModel;
import com.launay.ecoplant.viewmodels.PlantNetViewModel;
import com.launay.ecoplant.viewmodels.PlotViewModel;
import com.launay.ecoplant.viewmodels.ViewModel;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {
    private static final String ARG_PARAM1 = "plotID";

    private String plotID;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private Uri photoUri;
    PlantNetViewModel plantNetViewModel;
    PlotViewModel plotViewModel;
    ObservationViewModel observationViewModel;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    public PhotoFragment() {
    }

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


        plotViewModel = new ViewModelProvider(requireActivity()).get(PlotViewModel.class);


        plantNetViewModel = new ViewModelProvider(requireActivity()).get(PlantNetViewModel.class);
        observationViewModel = new ViewModelProvider(requireActivity()).get(ObservationViewModel.class);


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


        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            observationViewModel.setObsUriLiveData(selectedImageUri);
                            plantNetViewModel.loadPlantNetListLiveDataByUri(selectedImageUri);

                        }
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success) {
                        observationViewModel.setObsUriLiveData(photoUri);
                        plantNetViewModel.loadPlantNetListLiveDataByUri(photoUri);
                    }
                }
        );

        List<Plant> plants = new ArrayList<>();


        photoBtn.setOnClickListener(v->{
            checkPermissionAndOpenCamera();
        });

        galleryBtn.setOnClickListener(v->{
            openGallery();
        });

        switchPlotBtn.setOnClickListener(v->{
            Fragment fragment = new SwitchPlotPhotoFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack("switch_plot_fragment")
                    .commit();
        });

        plantRCV.setLayoutManager(new LinearLayoutManager(requireActivity()));
        plantRCV.setAdapter(new PlantAdapter(requireContext(),plants,getParentFragmentManager()));


        return view;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
    public void openCamera() {
        try {
            File photoFile = createImageFile();
            photoUri = FileProvider.getUriForFile(requireActivity(), "com.launay.ecoplant.fileprovider", photoFile);
            cameraLauncher.launch(photoUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }
    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    @Override //TODO regarder deprecate ici
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(requireContext(), "Permission caméra refusée", Toast.LENGTH_SHORT).show();
            }
        }
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
            plantName.setText(plant.getShortname());
            //TODO régler ça nbPlant.setText(""+plant.getNbPlant());
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
                if (observationViewModel.getObsUriLiveData().getValue()!=null){
                    observationViewModel.createObservation(plant,plotID,observationViewModel.getObsUriLiveData().getValue());
                }

            });
            knowmoreBtn.setOnClickListener(v->{
                //TODO renvoyer vers une page web
            });

        }
    }

}