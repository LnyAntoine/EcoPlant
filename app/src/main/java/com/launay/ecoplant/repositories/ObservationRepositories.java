package com.launay.ecoplant.repositories;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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

    public void createObservation(Plant plant,String plotId,Uri obsUri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null) {
            //On crée l'objet Observation à mettre en bdd
            Observation observation = new Observation(
                    null,
                    user.getUid(),
                    plant.getPlantId(),
                    new Date(),
                    plotId,
                    "",
                    plant
            );

            //On enregistre l'image de l'observation dans storage
            AtomicReference<String> photoUrl = new AtomicReference<>();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            String fileName = "images/" + UUID.randomUUID().toString() +"-1"+ ".jpg";
            StorageReference imageRef = storageRef.child(fileName);

            imageRef.putFile(obsUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            photoUrl.set(downloadUri.toString());
                            Log.d("FirebaseStorage", "Image URL: " + photoUrl.get());
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirebaseStorage", "Échec de l'upload", e);
                    });

            observation.setPhotoUrl(photoUrl.get());


            //On push l'objet dans la bdd

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("observations")
                    .add(observation)
                    .addOnSuccessListener(documentReference -> {
                        this.loadObservationById(documentReference.getId());
                    })
                    .addOnFailureListener(e->{
                        Log.e("FirebaseObservation","Erreur lors de l'ajout de "+observation);
                    })
            ;
        }
    }

    public void loadObservationById(String observationId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("observations").document(observationId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        currentObservation.setValue(documentSnapshot.toObject(Observation.class));
                    } else {
                        Log.d("FirestoreObservations","Il n'existe pas d'observation avec cet id "+observationId);
                    }
                })
                .addOnFailureListener(e->{
                    Log.e("FirestoreObservations","Erreur lors ouverture bdd "+e.getMessage());
                });
    }
    public void loadObservationListByPlotId(String plotId){
        List<Observation> observations = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("observations").whereEqualTo("plotid",plotId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                            observations.add(documentSnapshot.toObject(Observation.class));
                        }
                    } else {
                        Log.d("FirestoreObservations","Il n'existe pas d'observation sur ce plot "+plotId);

                    }
                })
                .addOnFailureListener(e->{
                    Log.e("FirestoreObservations","Erreur lors ouverture bdd "+e.getMessage());
                });
        observationList.setValue(observations);
    }

    public MutableLiveData<List<Observation>> getObservationList() {
        return observationList;
    }

    public MutableLiveData<Observation> getCurrentObservation() {
        return currentObservation;
    }
}
