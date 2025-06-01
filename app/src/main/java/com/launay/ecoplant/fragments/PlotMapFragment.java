package com.launay.ecoplant.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.collection.BuildConfig;
import com.launay.ecoplant.R;
import com.launay.ecoplant.models.Plot;
import com.launay.ecoplant.viewmodels.PlotViewModel;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;
import java.util.List;

public class PlotMapFragment extends Fragment implements LocationListener {
    private MapView mapView;
    private LocationManager locationManager;
    private PlotViewModel plotViewModel;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private double lat = 41.588811;
    private double lng = 9.284557;
    private List<Marker> markerList;
    private boolean isMoving = false;
    private IMapController mapController;
    private static final String ARG_PARAM1 = "lat";
    private static final String ARG_PARAM2 = "long";




    public static PlotMapFragment newInstance(Double param1,Double param2) {
        PlotMapFragment fragment = new PlotMapFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, param1);
        args.putDouble(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lat = getArguments().getDouble(ARG_PARAM1);
            lng = getArguments().getDouble(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        plotViewModel = new ViewModelProvider(requireActivity()).get(PlotViewModel.class);
        plotViewModel.refreshData();


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
                Plot p = (Plot) m.getRelatedObject();
                plotViewModel.updatePlot(p.getPlotId(),p);
            }
        });

        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        mapView = view.findViewById(R.id.map);

        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startGPS();
        }



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

        plotViewModel.getPlotsLiveData().observe(getViewLifecycleOwner(), plots -> {
            if (mapView!=null && !plots.isEmpty()) {
                clearMap();
                for (Plot plot : plots) {
                    createMarker(plot);
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

    private void createMarker(Plot plot){

        GeoPoint pos = new GeoPoint(plot.getLatitude(), plot.getLongitude());
        Marker marker = new Marker(mapView);
        marker.setPosition(pos);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setInfoWindow(new CustomInfoWindow(mapView,plot));
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.nature);
        drawable = DrawableCompat.wrap(drawable); // nécessaire pour le tint
        DrawableCompat.setTint(drawable, ContextCompat.getColor(requireContext(), R.color.mapPin_green));
        marker.setIcon(drawable);
        marker.setRelatedObject(plot);

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
                Plot p = (Plot) marker.getRelatedObject();
                p.setLongitude(newPosition.getLongitude());
                p.setLatitude(newPosition.getLatitude());
                marker.setRelatedObject(p);
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                // Optionnel, début du drag
            }
        });

        mapView.getOverlays().add(marker);
        markerList.add(marker);
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

        private TextView plotnameTV;
        private TextView detailsTV;
        private TextView getPlantMapTV;
        private ShapeableImageView picture;
        private ImageButton closeBtn;
        private Plot plot;

        public CustomInfoWindow(MapView mapView,Plot plot) {
            super(R.layout.plot_marker_info, mapView);
            this.plot = plot;
        }

        @Override
        public void onOpen(Object item) {
            Marker marker = (Marker) item;
            View view = getView();



            detailsTV = view.findViewById(R.id.details);
            getPlantMapTV = view.findViewById(R.id.plantMap);
            plotnameTV = view.findViewById(R.id.plot_name);
            closeBtn = view.findViewById(R.id.close);
            picture = view.findViewById(R.id.imageView);

            plotnameTV.setText(plot.getName());

            detailsTV.setOnClickListener(v->{
                if (isMoving){
                    Toast.makeText(requireContext(), "Veuillez valider les déplacements", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Fragment fragment = ManagePlotFragment.newInstance(false, plot.getPlotId());
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack("managePlotFragment")
                            .commit();
                }
            });

            getPlantMapTV.setOnClickListener(v->{
                if (isMoving){
                    Toast.makeText(requireContext(), "Veuillez valider les déplacements", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Fragment fragment = ObservationMapFragment.newInstance(plot.getPlotId());
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack("ObservationMapFragment")
                            .commit();
                }
            });

            closeBtn.setOnClickListener(v->{
                this.close();
            });

            Uri imageUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.jardin);
            String pictureUrl = plot.getPictureUrl() != null ? (plot.getPictureUrl().isEmpty() ? imageUri.toString() : plot.getPictureUrl()) : imageUri.toString();

            Glide.with(requireContext()).load(pictureUrl).fitCenter().into(picture);

        }

        @Override
        public void onClose() {
            // Optionnel : actions à la fermeture de la fenêtre
        }
    }
}