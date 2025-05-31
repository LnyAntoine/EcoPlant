package com.launay.ecoplant.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.launay.ecoplant.models.Plot;
import com.launay.ecoplant.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    public void createPlot(String name, Double lat, Double longi, String type,Consumer<String> callback){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Création de l'objet Plot
            Plot plot = new Plot(
                    type,               // type
                    name,     // name
                    null,                    // plotId (sera généré par Firestore)
                    userId,                  // ownerId
                    lat,                // latitude
                    longi,                 // longitude
                    0,                      // nbPlant
                    0.,                     // scoreAzote
                    0.,                     // scoreStruct
                    0.                     // scoreWater
            );

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
    public void deletePlot(String plotId, utils.AuthCallback callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Pas d'utilisateur connecté
            callback.onComplete(false);
            return;
        }

        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // On récupère la référence du document à supprimer
        DocumentReference plotRef = db.collection("plots").document(plotId);

        // Vérifier que l'utilisateur est bien le propriétaire (optionnel mais recommandé en local)
        plotRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String ownerId = documentSnapshot.getString("ownerId");
                if (userId.equals(ownerId)) {
                    // L'utilisateur est propriétaire, on peut supprimer
                    plotRef.delete()
                            .addOnSuccessListener(unused -> callback.onComplete(true))
                            .addOnFailureListener(e -> callback.onComplete(false));
                } else {
                    // Pas propriétaire
                    callback.onComplete(false);
                }
            } else {
                // Document inexistant
                callback.onComplete(false);
            }
        }).addOnFailureListener(e -> callback.onComplete(false));
    }

    public void reset(){
        this.plotsLiveData.setValue(null);
        this.currentPlotLiveData.setValue(null);
    }

}
