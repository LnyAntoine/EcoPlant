package com.launay.ecoplant.repositories;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.launay.ecoplant.models.Plot;
import com.launay.ecoplant.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PlotRepositories {
    private static PlotRepositories instance;
    private final MutableLiveData<List<Plot>> plotsLiveData;
    private final MutableLiveData<Plot> currentPlotLiveData ;
    private final MutableLiveData<Plot> plotLiveData;

    private PlotRepositories() {
        plotsLiveData  = new MutableLiveData<>();
        currentPlotLiveData = new MutableLiveData<>();
        currentPlotLiveData.setValue(null);
        plotLiveData = new MutableLiveData<>();
        plotLiveData.setValue(null);
        this.loadUserPlots();

    }

    public static synchronized PlotRepositories getInstance() {
        if (instance == null) {
            instance = new PlotRepositories();
        }
        return instance;
    }

    public void signOut(){
        instance = null;
    }

    public void updatePlot(String plotId,Plot plot){

        if (plot==null||plotId.isEmpty()){
            Log.e("UpdatePlot"," "+plot + " - "+ plotId);
            return;
        }
        Log.d("UpdatePlot",plot.toString());
        Log.d("UpdatePlot",plotId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> updates = new HashMap<>();
        updates.put("publicP", plot.getPublicP());
        updates.put("name", plot.getName());
        updates.put("pictureUrl", plot.getPictureUrl());
        updates.put("latitude", plot.getLatitude());
        updates.put("longitude", plot.getLongitude());
        updates.put("nbPlant", plot.getNbPlant());

        db.collection("plots").document(plotId).update(updates)
                .addOnSuccessListener(aVoid -> Log.d("UpdatePlot", "Plot mis à jour avec succès"))
                .addOnFailureListener(e -> Log.e("UpdatePlot", "Erreur lors de la mise à jour du plot", e));
        ;

    }

    public void loadPlotById(String plotId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("plots")
                .document(plotId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Plot plot = documentSnapshot.toObject(Plot.class);
                        plot.setPlotId(documentSnapshot.getId());
                        plotLiveData.setValue(plot);
                    }
                    else {
                        Log.d("PlotRepo","CurrentPlot : Document not found");
                        plotLiveData.setValue(null);
                    }

                })
                .addOnFailureListener(e -> {
                    Log.d("PlotRepo","Current Plot : AddOnFailureListener"+e.getMessage());
                    plotLiveData.postValue(null);
                });


    }
    public LiveData<Plot> getPlotById(){
        return plotLiveData;
    }
    public void createPlot(String name, Uri pictureUri, Double lat, Double longi, Boolean publicP, Consumer<String> callback){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Création de l'objet Plot
            Plot plot = new Plot(
                    publicP,               // publicP
                    name,     // name
                    null,                    // plotId (sera généré par Firestore)
                    userId,                  // ownerId
                    lat,                // latitude
                    longi,                 // longitude
                    0                      // nbPlant
            );

            utils.uploadImage("/plots/",pictureUri,url ->{
                if (!url.isEmpty()){plot.setPictureUrl(url);}
                // Ajout du plot dans la collection
                db.collection("plots")
                        .add(plot)
                        .addOnSuccessListener(documentReference -> {
                            Log.d("Firestore", "Plot créé avec ID: " + documentReference.getId());
                            this.loadCurrentPlot(documentReference.getId());
                            callback.accept(documentReference.getId());
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firestore", "Erreur lors de la création du plot", e);
                            callback.accept("");
                        });
            });




        }
        else {
            callback.accept("");
        }
    }
    public LiveData<List<Plot>> getPlotsLiveData(){
        return this.plotsLiveData;
    }
    public MutableLiveData<Plot> getCurrentPlotLiveData(){
        return this.currentPlotLiveData;
    }

    public void loadCurrentPlot(String plotId){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("plots")
                .document(plotId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Plot plot = documentSnapshot.toObject(Plot.class);
                        plot.setPlotId(documentSnapshot.getId());
                        currentPlotLiveData.setValue(plot);
                    }
                    else {
                        Log.d("PlotRepo","CurrentPlot : Document not found");
                        currentPlotLiveData.setValue(null);
                    }

                })
                .addOnFailureListener(e -> {
                    Log.d("PlotRepo","Current Plot : AddOnFailureListener"+e.getMessage());
                    currentPlotLiveData.postValue(null);
                });

    }

    public void loadUserPlots(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("plots")
                    .whereEqualTo("ownerId", userId)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        List<Plot> plots = new ArrayList<>();
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            Plot plot = doc.toObject(Plot.class);
                            if (plot != null) {
                                plot.setPlotId(doc.getId()); // si tu veux conserver l'ID du document
                                plots.add(plot);
                            }
                        }
                        plotsLiveData.setValue(plots);
                    })
                    .addOnFailureListener(e -> {
                        Log.d("PlotRepo","AllPlots : addOnFailureListener "+e.getMessage());
                        plotsLiveData.setValue(Collections.emptyList());
                    });
        } else {
            Log.d("PlotRepo","AllPlots : user == null "+user);

        }

    }
    public void deletePlot(String plotId, Consumer<Boolean> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("plots").document(plotId).delete()
                .addOnSuccessListener(avoid -> callback.accept(true))
                .addOnFailureListener(e -> {
                    Log.e("deletePlot"," "+e.getMessage());
                    callback.accept(false);
                });
    }

    public void reset(){
        this.plotsLiveData.setValue(null);
        this.currentPlotLiveData.setValue(null);
    }

    public void refreshData(){
        if (currentPlotLiveData.getValue()!=null){loadCurrentPlot(currentPlotLiveData.getValue().getPlotId());}
        if (plotLiveData.getValue()!=null){loadPlotById(plotLiveData.getValue().getPlotId());}
        loadUserPlots();
    }


}
