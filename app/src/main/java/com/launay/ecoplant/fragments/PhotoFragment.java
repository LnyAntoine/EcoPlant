package com.launay.ecoplant.fragments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.imageview.ShapeableImageView;
import com.launay.ecoplant.R;
import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.viewmodels.ObservationViewModel;
import com.launay.ecoplant.viewmodels.PlantNetViewModel;
import com.launay.ecoplant.viewmodels.PlotViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

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
    private PlantAdapter adapter;
    PlantNetViewModel plantNetViewModel;
    PlotViewModel plotViewModel;
    ObservationViewModel observationViewModel;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_LOCATION_PERMISSION = 101;
    private static final int REQUEST_GALLERY_PERMISSION = 102;

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

        plantNetViewModel.getPlantNetListLiveData().observe(requireActivity(),plantList -> {
            Log.d("plantNetlistobserver","observing"+plantList);
            if (!plantList.isEmpty() && adapter !=null){
                adapter.updateList(plantList);
            }
        });

        plotViewModel.getCurrentPlotLiveData().observe(requireActivity(),p -> {
            if (p!=null){
                Log.d("currentplot",p.toString());
                plotID = p.getPlotId();
                currentPlotView.setVisibility(VISIBLE);
                plotName.setText(p.getName());
                plotNbPlant.setText(p.getNbPlant()+" plantes");
                Log.d("currentplot"," "+plotID);
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
                            plantNetViewModel.loadPlantNetListLiveDataByUri(requireContext(),selectedImageUri);
                            checkLocationPermissionAndGetLocation();
                        }
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success) {
                        observationViewModel.setObsUriLiveData(photoUri);
                        plantNetViewModel.loadPlantNetListLiveDataByUri(requireContext(),photoUri);
                        checkLocationPermissionAndGetLocation();
                    }
                }
        );
        observationViewModel.getObsUriLiveData().observe(getViewLifecycleOwner(), uri -> {
            adapter.notifyDataSetChanged();
        });

        observationViewModel.getObservationLocationLiveData().observe(getViewLifecycleOwner(), loc -> {
            adapter.notifyDataSetChanged();
        });



        photoBtn.setOnClickListener(v->{
            checkPermissionAndOpenCamera();
        });

        galleryBtn.setOnClickListener(v->{
            checkPermissionAndOpenGallery();
        });

        switchPlotBtn.setOnClickListener(v->{
            Fragment fragment = new SwitchPlotPhotoFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack("switch_plot_fragment")
                    .commit();
        });
        adapter = new PlantAdapter(requireContext(),plantNetViewModel.getPlantNetListLiveData().getValue(),getParentFragmentManager()) ;

        plantRCV.setLayoutManager(new LinearLayoutManager(requireActivity()));
        plantRCV.setAdapter(adapter);


        return view;
    }

    private void getLocationAndSave() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("PhotoFragment", "Permission de localisation non accordée");
            return;
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(500)
                .setNumUpdates(1);

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        observationViewModel.setObservationLocationLiveData(location);
                        Log.d("PhotoFragment", "Location set: " + location.getLatitude() + ", " + location.getLongitude());
                    }
                    fusedLocationClient.removeLocationUpdates(this);
                }
            }, Looper.getMainLooper());
        } catch (SecurityException e) {
            Log.e("PhotoFragment", "SecurityException lors de la récupération de la localisation", e);
        }
    }

    private void checkLocationPermissionAndGetLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocationAndSave(); // permission déjà accordée
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
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

    private void checkPermissionAndOpenGallery(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    REQUEST_GALLERY_PERMISSION); // ton requestCode
        } else {
            openGallery(); // si déjà accordée
        }
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
        } else if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndSave(); // permission maintenant accordée
            } else {
                Toast.makeText(requireContext(), "Permission de localisation refusée", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == REQUEST_GALLERY_PERMISSION ){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery(); // permission maintenant accordée
            } else {
                Toast.makeText(requireContext(), "Permission de localisation refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        plotViewModel.refreshData();
        observationViewModel.refreshData((plotViewModel.getCurrentPlotLiveData().getValue()!=null) ? plotViewModel.getCurrentPlotLiveData().getValue().getPlotId() : "");
    }

    public class PlantAdapter extends RecyclerView.Adapter<PlantViewHolder> {

        private List<Plant> plantList;
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
            Plant plant = plantList.get(position);
            Uri obsUri = observationViewModel.getObsUriLiveData().getValue();
            Location location = observationViewModel.getObservationLocationLiveData().getValue();

            holder.bind(plant, obsUri, location, p -> {
                observationViewModel.createObservation(p, plotID, obsUri,1);
            });

        }

        @Override
        public int getItemCount() {
            return plantList.size(); // Correction ici
        }
        public void updateList(List<Plant> newList) {
            this.plantList = newList;
            notifyDataSetChanged(); // redessine tout
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
        private final ShapeableImageView pictureView;
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
            this.pictureView = itemView.findViewById(R.id.imageView);
        }

        public void bind(Plant plant, Uri obsUri, Location location, Consumer<Plant> onAddClicked) {
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

            boolean enabled = obsUri != null && location != null;
            addBtn.setEnabled(enabled);

            addBtn.setOnClickListener(v -> {
                if (enabled) {
                    onAddClicked.accept(plant);
                } else {
                    Toast.makeText(itemView.getContext(), "Données non prêtes", Toast.LENGTH_SHORT).show();
                }
            });
            knowmoreBtn.setOnClickListener(v->{
                //TODO renvoyer vers une page web
            });

            Uri imageUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.pissenlit);
            String pictureUrl = plant.getPictureUrl().isEmpty()?imageUri.toString():plant.getPictureUrl();

            Glide.with(requireContext())
                    .load(pictureUrl)
                    .fitCenter()
                    .into(pictureView);

        }
    }


}