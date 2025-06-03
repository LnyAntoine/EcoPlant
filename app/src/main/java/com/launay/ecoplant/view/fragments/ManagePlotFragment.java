package com.launay.ecoplant.view.fragments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.launay.ecoplant.R;
import com.launay.ecoplant.models.entity.Observation;
import com.launay.ecoplant.models.entity.Plant;
import com.launay.ecoplant.models.entity.Plot;
import com.launay.ecoplant.viewmodels.ObservationViewModel;
import com.launay.ecoplant.viewmodels.PlotViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
    private PlotViewModel plotViewModel;

    private ObservationViewModel observationViewModel;

    private Uri photoUri;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_LOCATION_PERMISSION = 101;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
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
    public void onPause() {

        super.onPause();
        Plot plot = plotViewModel.getPlotById().getValue();
        if (plot!=null){
            Log.d("onPauseMPF",plot.toString());
            int pltCount = 0;
            for(Observation obs : adapter.getObservations()){
                pltCount+=obs.getNbPlantes();
                observationViewModel.updateObservation(plot.getPlotId(),obs.getObservationId(),obs);

            }

            Uri imageUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.jardin);
            photoUri = photoUri==null?
                    Uri.parse(plot.getPictureUrl())==null?
                    imageUri:Uri.parse(plot.getPictureUrl()):photoUri;
            plot.setPictureUrl(photoUri.toString());
            plot.setNbPlant(pltCount);
            plotViewModel.updatePlot(plot.getPlotId(),plot);

        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        ShapeableImageView picture = view.findViewById(R.id.imageView);
        TextView latTV = view.findViewById(R.id.latitudeVal);
        TextView longTV = view.findViewById(R.id.longitudeVal);
        EditText nameField = view.findViewById(R.id.name_field);

        plotViewModel.getPlotById().observe(getViewLifecycleOwner(),plot ->{
            Log.d("getPlotById"," "+plot);
            if (plot!=null) {
                latTV.setText(plot.getLatitude()+"");
                longTV.setText(plot.getLongitude()+"");
                nameField.setText(plot.getName());
                photoUri = Uri.parse(plot.getPictureUrl());
                Glide.with(requireContext()).load(plot.getPictureUrl()).fitCenter().into(picture);
                observationViewModel.loadObservationListLiveDataByPlotId(plot.getPlotId());
            }
        });

        observationViewModel.getObservationListLiveData().observe(getViewLifecycleOwner(),observationList -> {
            Log.d("getObservationListLiveData","" + observationList);
            if (!observationList.isEmpty()){
                if (Objects.equals(observationList.get(0).getPlotId(), plotID)){adapter.updateList(observationList);}
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_plot, container, false);

        Button changePictureBtn = view.findViewById(R.id.change_picture_btn);
        EditText nameField = view.findViewById(R.id.name_field);
        Button deleteBtn = view.findViewById(R.id.delete_btn);
        ImageButton returnBtn = view.findViewById(R.id.return_btn);
        Button addPlant = view.findViewById(R.id.add_plant_btn);
        RecyclerView plantListRCV = view.findViewById(R.id.plant_list);
        ShapeableImageView picture = view.findViewById(R.id.imageView);
        Button mapBtn  = view.findViewById(R.id.mapBtn);
        TextView latTV = view.findViewById(R.id.latitudeVal);
        TextView longTV = view.findViewById(R.id.longitudeVal);

        plantListRCV.setLayoutManager(new LinearLayoutManager(requireActivity()));

        adapter = new ObservationAdapter(requireContext(),new ArrayList<>(),getParentFragmentManager());

        plantListRCV.setAdapter(adapter);

        plotViewModel = new ViewModelProvider(requireActivity()).get(PlotViewModel.class);
        plotViewModel.loadPlotByid(plotID);

        observationViewModel = new ViewModelProvider(requireActivity()).get(ObservationViewModel.class);



        mapBtn.setOnClickListener(v->{
            if (isAdded()){
                Plot plot = plotViewModel.getPlotById().getValue();
                if (plot!=null){
                    Fragment fragment = PlotMapFragment.newInstance(plot.getLatitude(),plot.getLongitude());
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack("PlotMapFragment")
                            .commit();
                }
            }
        });

        nameField.setActivated(false);

        addPlant.setOnClickListener(v->{
            plotViewModel.loadCurrentPlot(plotID);
            Fragment fragment = new PhotoFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack("photo_fragment")
                    .commit();
        });

        returnBtn.setOnClickListener(v->{
            if (fromCreate){
                getParentFragmentManager().popBackStack();
            }
            getParentFragmentManager().popBackStack();
        });

        deleteBtn.setOnClickListener(v->{
            Plot plot = plotViewModel.getPlotById().getValue();
            if (plot != null){
                plotViewModel.deletePlot(plot.getPlotId(),deleted -> {
                    if (deleted){
                        if (fromCreate){
                            getParentFragmentManager().popBackStack();
                        }
                        getParentFragmentManager().popBackStack();
                    }
                });
            }
        });


        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            photoUri=selectedImageUri;
                            Glide.with(requireContext()).load(photoUri).fitCenter().into(picture);
                        }
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    Glide.with(requireContext()).load(photoUri).fitCenter().into(picture);
                }
        );



        changePictureBtn.setOnClickListener(v->{
            showImagePickerDialog();
        });
        return view;
    }
    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choisir une option")
                .setItems(new CharSequence[]{"Prendre une photo", "Choisir dans la galerie"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            checkPermissionAndOpenCamera();
                        } else if (which == 1) {
                            openGallery();
                        }
                    }
                });
        builder.create().show();
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }
    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    @Override
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




    public class ObservationAdapter extends RecyclerView.Adapter<ObservationViewHolder> {

        private final List<Observation> observations;
        private final Context ctx;
        private final FragmentManager fragmentManager;

        public List<Observation> getObservations(){return observations;}

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
        private final Button delete;
        private final ImageButton add;
        private final ImageButton remove;
        private final ShapeableImageView picture;
        private int plantCount;




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
            this.delete = itemView.findViewById(R.id.removeObsBtn);
            this.picture = itemView.findViewById(R.id.imageView);
            this.add = itemView.findViewById(R.id.addOne);
            this.remove = itemView.findViewById(R.id.removeoneBtn);
        }

        public void bind(Observation obs, ObservationAdapter adapter) {
            Plant plant = obs.getPlant();
            plantCount=obs.getNbPlantes();

            plantName.setText(plant.getShortname());
            nbPlant.setText(""+obs.getNbPlantes());
            plantFullName.setText(plant.getFullname());
            azoteScore.setText(String.format("%.2f", plant.getScoreAzote()));
            waterScore.setText(String.format("%.2f", plant.getScoreWater()));
            groundScore.setText(String.format("%.2f", plant.getScoreStruct()));
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

            add.setOnClickListener(v->{
                plantCount++;
                nbPlant.setText(""+plantCount);
                obs.setNbPlantes(plantCount);
            });
            remove.setOnClickListener(v->{
                plantCount = plantCount==0?0:plantCount-1;
                nbPlant.setText(""+plantCount);
                obs.setNbPlantes(plantCount);
            });

            delete.setOnClickListener(v->{
                delete.setEnabled(false);
                observationViewModel.deleteObservationById(plotID,obs.getObservationId(),b ->{
                    delete.setEnabled(true);
                    if (b) {
                        observationViewModel.loadObservationListLiveDataByPlotId(plotID);
                    }
                });
            });
            knowmoreBtn.setOnClickListener(v->{
                String url = plant.getDetailsLink();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            });

            Glide.with(requireActivity())
                    .load(obs.getPictureUrl())
                    .fitCenter()
                    .into(picture);

        }
    }

}