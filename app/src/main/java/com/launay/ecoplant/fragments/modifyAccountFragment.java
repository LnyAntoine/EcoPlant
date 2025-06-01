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
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.launay.ecoplant.models.User;
import com.launay.ecoplant.viewmodels.PlotViewModel;
import com.launay.ecoplant.viewmodels.UserViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link modifyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class modifyAccountFragment extends Fragment {

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

    public modifyAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment modifyAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static modifyAccountFragment newInstance(String param1, String param2) {
        modifyAccountFragment fragment = new modifyAccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_modify_account, container, false);
        ShapeableImageView pfpIV = view.findViewById(R.id.pfpField);
        EditText fullNameField = view.findViewById(R.id.fullNameField);
        EditText displayNameField = view.findViewById(R.id.displayNameField);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        Button validateBtn = view.findViewById(R.id.validate_btn);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(),user -> {
            if (user!=null){
                fullNameField.setText(user.getFullname());
                displayNameField.setText(user.getDisplayName());

                Uri imageUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.ic_launcher_foreground);
                String pictureUrl = user.getPfpURL()!=null?
                        (user.getPfpURL().isEmpty()?imageUri.toString():user.getPfpURL()):imageUri.toString();

                Glide.with(requireActivity()).load(pictureUrl).fitCenter().into(pfpIV);
            }
        });

        pfpIV.setOnClickListener(v->{
            showImagePickerDialog();
        });

        cancelBtn.setOnClickListener(v->{
            getParentFragmentManager().popBackStack();
        });
        validateBtn.setOnClickListener(v->{
            validateBtn.setEnabled(false);

            User user = userViewModel.getCurrentUser().getValue();
            if (user!=null){
                Uri imageUri = Uri.parse(user.getPfpURL());
                photoUri = photoUri==null?imageUri:photoUri;
                user.setFullname(fullNameField.getText().toString().isEmpty()?user.getFullname():fullNameField.getText().toString());
                user.setDisplayName(displayNameField.getText().toString().isEmpty()?user.getDisplayName():displayNameField.getText().toString());
                user.setPfpURL(photoUri.toString());
            }
            userViewModel.updateUser(user,aBoolean -> {

            });
            validateBtn.setEnabled(true);
            userViewModel.loadCurrentUser();
            getParentFragmentManager().popBackStack();
        });

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            photoUri=selectedImageUri;
                            Glide.with(requireContext()).load(photoUri).fitCenter().into(pfpIV);
                        }
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    Glide.with(requireContext()).load(photoUri).fitCenter().into(pfpIV);
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
}