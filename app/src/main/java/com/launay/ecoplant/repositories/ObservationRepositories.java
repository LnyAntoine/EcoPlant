package com.launay.ecoplant.repositories;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.launay.ecoplant.models.Observation;
import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ObservationRepositories {

    private MutableLiveData<List<Observation>> observationList;
    private MutableLiveData<Observation> currentObservation;
    private static ObservationRepositories instance;

    public static ObservationRepositories getInstance(){
        if (instance==null){
            instance = new ObservationRepositories();
        }
        return instance;
    }
    private ObservationRepositories(){
        this.observationList = new MutableLiveData<>();
        this.currentObservation = new MutableLiveData<>();
        observationList.setValue(new ArrayList<>());
        currentObservation.setValue(null);
    }

    public void signOut(){
        instance = null;
    }

    public void updateObservation(String plotId,String obsId,Observation observation){
        if (observation==null||plotId.isEmpty()||obsId.isEmpty()){
            Log.e("updateObservation"," "+observation + " - "+ plotId +" - "+obsId);
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> updates = new HashMap<>();
        updates.put("notes", observation.getNotes());
        updates.put("pictureUrl", observation.getPictureUrl());
        updates.put("lat", observation.getLat());
        updates.put("longi", observation.getLongi());
        updates.put("nbPlantes", observation.getNbPlantes());
        updates.put("plantId", observation.getPlantId());
        updates.put("date", observation.getDate());


        db.collection("plots")
                .document(plotId)
                .collection("observations")
                .document(obsId)
                .update(updates)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Observation mise à jour avec succès"))
                .addOnFailureListener(e -> Log.e("Firestore", "Erreur mise à jour Observation", e));

    }

    public void createObservation(Plant plant, String plotId, Uri obsUri, Location location, int nbPlantes,Consumer<Boolean> callback){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null && !plotId.isEmpty()) {
            //On crée l'objet Observation à mettre en bdd
            Observation observation = new Observation(
                    null,
                    user.getUid(),
                    plant.getPlantId(),
                    new Date(),
                    plotId,
                    "",
                    plant,
                    nbPlantes
            );
            observation.setLat(location.getLatitude());
            observation.setLongi(location.getLongitude());


            utils.uploadImage("/plots/"+plotId+"/observations/",obsUri, url -> {
                if (!url.isEmpty()){observation.setPictureUrl(url);}
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("plots")
                        .document(plotId)
                        .collection("observations")
                        .add(observation)
                        .addOnSuccessListener(documentReference -> {
                            this.loadObservationById(plotId,documentReference.getId());
                            callback.accept(true);
                        })
                        .addOnFailureListener(e->{
                            Log.e("FirebaseObservation","Erreur lors de l'ajout de "+observation);
                            currentObservation.setValue(null);
                            callback.accept(false);
                        })
                ;
            });
        }
    }

    public void loadObservationById(String plotId,String observationId){
        if (plotId.isEmpty() || observationId.isEmpty()){
            currentObservation.setValue(null);
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("plots")
                .document(plotId)
                .collection("observations")
                .document(observationId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        Observation observation = documentSnapshot.toObject(Observation.class);
                        if (observation!=null) {observation.setObservationId(documentSnapshot.getId());}
                        currentObservation.setValue(observation);
                    } else {
                        Log.d("FirestoreObservations","Il n'existe pas d'observation avec cet id "+observationId);
                    }
                })
                .addOnFailureListener(e->{
                    Log.e("FirestoreObservations","Erreur lors ouverture bdd "+e.getMessage());
                    currentObservation.setValue(null);
                });
    }
    public void loadObservationListByPlotId(String plotId){
        List<Observation> observations = new ArrayList<>();
        if (plotId.isEmpty()){
            observationList.setValue(observations);
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("plots")
                .document(plotId)
                .collection("observations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                            Observation observation = documentSnapshot.toObject(Observation.class);
                            if (observation!=null) {observation.setObservationId(documentSnapshot.getId());}
                            observations.add(observation);
                        }
                        observationList.setValue(observations);
                    } else {
                        Log.d("FirestoreObservations","Il n'existe pas d'observation sur ce plot "+plotId);
                    }
                })
                .addOnFailureListener(e->{
                    Log.e("FirestoreObservations","Erreur lors ouverture bdd "+e.getMessage());
                    observationList.setValue(observations);
                });
    }
    public void deleteObservationById(String plotId,String obsId,Consumer<Boolean> callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (plotId.isEmpty()){
            callback.accept(false);
            return;
        }
        db.collection("plots")
                .document(plotId)
                .collection("observations")
                .document(obsId)
                .delete()
                .addOnSuccessListener( avoid -> {
                    callback.accept(true);
                })
                .addOnFailureListener(e->{
                    Log.e("deleteObservationById"," " + e.getMessage());
                    callback.accept(false);
                });
    }

    public MutableLiveData<List<Observation>> getObservationList() {
        return observationList;
    }

    public MutableLiveData<Observation> getCurrentObservation() {
        return currentObservation;
    }
    public void refreshData(String plotId){
        if (plotId.isEmpty()){
            currentObservation.setValue(null);
            observationList.setValue(new ArrayList<>());
            return;
        }
        if (currentObservation.getValue()!=null){loadObservationById(plotId,currentObservation.getValue().getObservationId());}
        loadObservationListByPlotId(plotId);
    }

}
