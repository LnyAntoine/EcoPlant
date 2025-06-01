package com.launay.ecoplant.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.launay.ecoplant.viewmodels.PlotViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private PlotViewModel plotViewModel;
    private Uri photoUri;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_LOCATION_PERMISSION = 101;

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

        plotViewModel = new ViewModelProvider(requireActivity()).get(PlotViewModel.class);

        checkLocationPermissionAndGetLocation();

        modifyPictureBtn.setOnClickListener(v->{
            showImagePickerDialog();
        });

        createBtn.setOnClickListener(v->{
            Location location = plotViewModel.getPlotLocationLiveData().getValue();
            if (location==null){
                getParentFragmentManager().popBackStack();
                return;
            }
            if (changeName.getText()==null){
                return;
            }
            createBtn.setEnabled(false);
            Uri imageUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.jardin);
            photoUri = photoUri==null?imageUri:photoUri;
            plotViewModel.createPlot(changeName.getText().toString(),photoUri,location.getLatitude(),location.getLongitude(),false,plotid -> {
                createBtn.setEnabled(true);
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

    private void getLocationAndSave() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("CreatePlotFragment", "Permission de localisation non accordée");
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
                        plotViewModel.setPlotLocationLiveData(location);
                        Log.d("CreatePlotFragment", "Location set: " + location.getLatitude() + ", " + location.getLongitude());
                    }
                    fusedLocationClient.removeLocationUpdates(this);
                }
            }, Looper.getMainLooper());
        } catch (SecurityException e) {
            Log.e("CreatePlotFragment", "SecurityException lors de la récupération de la localisation", e);
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

        }
    }
}