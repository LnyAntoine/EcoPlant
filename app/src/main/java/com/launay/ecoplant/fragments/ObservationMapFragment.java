package com.launay.ecoplant.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.launay.ecoplant.R;
import com.launay.ecoplant.models.Observation;
import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.models.Plot;
import com.launay.ecoplant.viewmodels.ObservationViewModel;
import com.launay.ecoplant.viewmodels.PlotViewModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ObservationMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ObservationMapFragment extends Fragment implements LocationListener {

    private MapView mapView;
    private LocationManager locationManager;
    private PlotViewModel plotViewModel;
    private ObservationViewModel observationViewModel;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private IMapController mapController;

    private double lat = 41.588811;
    private double lng = 9.284557;

    private static final String ARG_PARAM1 = "plotId";
    private String plotId;
    private String mParam2;
    private Plot plot;
    private boolean isMoving = false;

    private List<Marker> markerList;



    public ObservationMapFragment() {
        // Required empty public constructor
    }

    public static ObservationMapFragment newInstance(String param1) {
        ObservationMapFragment fragment = new ObservationMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plotId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_observation_map, container, false);
        plotViewModel = new ViewModelProvider(requireActivity()).get(PlotViewModel.class);
        observationViewModel = new ViewModelProvider(requireActivity()).get(ObservationViewModel.class);
        ImageButton returnBtn = view.findViewById(R.id.return_btn);

        returnBtn.setOnClickListener(v->{
            if (isMoving){
                Toast.makeText(requireContext(), "Veuillez valider les déplacements", Toast.LENGTH_SHORT).show();

            } else {
                getParentFragmentManager().popBackStack();
            }
        });

        Button moveBtn = view.findViewById(R.id.move_btn);
        Button validateBtn = view.findViewById(R.id.validate_btn);

        moveBtn.setOnClickListener(v->{
            moveBtn.setVisibility(GONE);
            moveBtn.setEnabled(false);
            validateBtn.setVisibility(VISIBLE);
            validateBtn.setEnabled(true);
            isMoving = true;
            for (Marker m : markerList){
                m.setDraggable(true);
            }
        });
        validateBtn.setOnClickListener(v->{
            validateBtn.setVisibility(GONE);
            validateBtn.setEnabled(false);
            moveBtn.setVisibility(VISIBLE);
            moveBtn.setEnabled(true);
            isMoving = false;
            for (Marker m : markerList){
                m.setDraggable(false);
                Observation obs = (Observation) m.getRelatedObject();
                observationViewModel.updateObservation(obs.getPlotId(),obs.getObservationId(),obs);
            }
        });


        plotViewModel.loadPlotByid(plotId);

        plotViewModel.getPlotById().observe(getViewLifecycleOwner(),plot1 -> {
                if (plot1!=null&& isAdded()){
                    plot = plot1;
                    lat = plot1.getLatitude();
                    lng = plot1.getLongitude();
                    observationViewModel.loadObservationListLiveDataByPlotId(plotId);
                }
        });




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.map);

        SharedPreferences prefs = requireContext().getSharedPreferences("map_state", Context.MODE_PRIVATE);
        float lat = prefs.getFloat("lat", 0f);
        float lon = prefs.getFloat("lon", 0f);
        float zoom = prefs.getFloat("zoom", 10f); // par défaut zoom 10

        GeoPoint savedPoint = new GeoPoint(lat, lon);
        mapView.getController().setZoom(zoom);
        mapView.getController().setCenter(savedPoint);

        observationViewModel.getObservationListLiveData().observe(getViewLifecycleOwner(),observations -> {
            if (mapView!=null && !observations.isEmpty()&& isAdded()){
                clearMap();
                for (Observation obs: observations){
                    createMarker(obs);
                }
            }
        });
    }

    private void startGPS() {
        if (mapView == null) {
            Log.e("startGPS", "mapView est null !");
            return;
        }
        Log.d("start GPS", "Initialisation du GPS...");
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapController = mapView.getController();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

            Log.d("GPS Start", "Activé");

            updateMap(lat, lng);
        } else {
            Log.e("StartGPS", "Permission non accordée");
        }
    }

    private void createMarker(Observation obs){
        GeoPoint pos = new GeoPoint(obs.getLat(), obs.getLongi());
        Marker marker = new Marker(mapView);
        marker.setPosition(pos);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setInfoWindow(new CustomInfoWindow(mapView,obs));
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.cannabis);
        drawable = DrawableCompat.wrap(drawable); // nécessaire pour le tint
        DrawableCompat.setTint(drawable, ContextCompat.getColor(requireContext(), R.color.mapPin_green));
        marker.setIcon(drawable);
        marker.setRelatedObject(obs);

        marker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {
                // Optionnel, pendant le déplacement
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                GeoPoint newPosition = marker.getPosition();
                Log.d("Marker", "Nouvelle position : " + newPosition.getLatitude() + ", " + newPosition.getLongitude());

                // → Tu peux sauvegarder la position ici (BDD, ViewModel, etc.)
                Observation obs = (Observation) marker.getRelatedObject();
                obs.setLongi(newPosition.getLongitude());
                obs.setLat(newPosition.getLatitude());
                marker.setRelatedObject(obs);
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                // Optionnel, début du drag
            }
        });

        markerList.add(marker);
        mapView.getOverlays().add(marker);
    }

    private void clearMap(){
        markerList = new ArrayList<>();
        mapView.getOverlays().clear();

    }

    private void updateMap(double latitude, double longitude) {
        GeoPoint pos = new GeoPoint(latitude, longitude);
        mapController.setZoom(15);
        mapController.setCenter(pos);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGPS();
            } else {
                Log.e("GPS Permission", "Permission refusée");
                Toast.makeText(requireContext(), "Pas de permission, pas de GPS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        GeoPoint center = (GeoPoint) mapView.getMapCenter();
        double lat = center.getLatitude();
        double lon = center.getLongitude();
        double zoom = mapView.getZoomLevelDouble();

        SharedPreferences prefs = requireContext().getSharedPreferences("map_state", Context.MODE_PRIVATE);
        prefs.edit()
                .putFloat("lat", (float) lat)
                .putFloat("lon", (float) lon)
                .putFloat("zoom", (float) zoom)
                .apply();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("Location changed", "Nouvelle position: " + location.toString());
        updateMap(location.getLatitude(), location.getLongitude());
    }

    public class CustomInfoWindow extends InfoWindow {

        private TextView titleView;
        private TextView descriptionView;

        private TextView plantName;
        private TextView plantFullName;
        private TextView nbPlant;
        private TextView azoteScore;
        private TextView groundScore;
        private TextView waterScore;
        private Button knowmoreBtn;

        private TextView detailsTV;
        private TextView getPlantMapTV;
        private ShapeableImageView picture;
        private ImageButton closeBtn;
        private Observation obs;
        private View detailedField;

        public CustomInfoWindow(MapView mapView,Observation obs) {
            super(R.layout.observation_marker_info, mapView);
            this.obs = obs;
        }

        @Override
        public void onOpen(Object item) {
            Marker marker = (Marker) item;
            View itemView = getView();

            Uri imageUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.pissenlit);
            String pictureUrl = obs.getPictureUrl() != null ? (obs.getPictureUrl().isEmpty() ? imageUri.toString() : obs.getPictureUrl()) : imageUri.toString();


            this.plantFullName = itemView.findViewById(R.id.full_name_value);
            this.detailedField = itemView.findViewById(R.id.detailed_field);
            this.plantName = itemView.findViewById(R.id.name_field);
            this.nbPlant = itemView.findViewById(R.id.nb_value);
            this.detailsTV = itemView.findViewById(R.id.details_field);
            this.azoteScore = itemView.findViewById(R.id.azote_score);
            this.waterScore = itemView.findViewById(R.id.water_score);
            this.groundScore = itemView.findViewById(R.id.ground_score);
            this.knowmoreBtn =itemView.findViewById(R.id.knowmore_btn);
            this.picture = itemView.findViewById(R.id.imageView);
            this.closeBtn = itemView.findViewById(R.id.close);

            Plant plant = obs.getPlant();
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

            detailsTV.setOnClickListener(v -> {
                if (detailedField.getVisibility() == GONE){
                    detailedField.setVisibility(VISIBLE);
                } else {
                    detailedField.setVisibility(GONE);
                }
            });

            knowmoreBtn.setOnClickListener(v->{
                String url = plant.getDetailsLink();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            });

            closeBtn.setOnClickListener(v->{
                this.close();
            });

            Glide.with(requireContext()).load(pictureUrl).fitCenter().into(picture);

        }

        @Override
        public void onClose() {
            // Optionnel : actions à la fermeture de la fenêtre
        }
    }
}