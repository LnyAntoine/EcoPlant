package com.launay.ecoplant.repositories;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.models.PlantNetResult;
import com.launay.ecoplant.models.PlantFullService;
import com.launay.ecoplant.models.PlantService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class PlantNetRepositories {
    private final FirebaseFirestore db;
    private static PlantNetRepositories instance;
    private MutableLiveData<List<Plant>> plantNetList;
    public static synchronized PlantNetRepositories getInstance(){
        if (instance == null){
            instance = new PlantNetRepositories();
        }
        return instance;
    }
    private PlantNetRepositories(){
        this.plantNetList = new MutableLiveData<>();
        this.plantNetList.setValue(new ArrayList<>());
         db = FirebaseFirestore.getInstance();
    }

    private Plant createPlant(String shortname,String fullname, String gbifId,Double scoreAzote,
                              Double scoreStruct, double reliabilityWater, double reliabilityGround,
                              double reliabilityAzote, Double scoreWater){
        AtomicReference<Plant> plantReturn = new AtomicReference<>();
        Plant plant = new Plant(
                null, //Sera générer par Firestore
                shortname,
                fullname,
                gbifId,
                scoreAzote,
                reliabilityAzote,
                scoreStruct,
                reliabilityGround,
                scoreWater,
                reliabilityWater
        );
        db.collection("plants")
                .add(plant)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FireStorePlant","Plante crée "+ plant);
                    plantReturn.set(getPlantById(documentReference.getId()));
                })
                .addOnFailureListener(e -> {
                    Log.e("FireStorePlant","Plante non crée "+e.getMessage());
                    plantReturn.set(null);
                });
        return plantReturn.get();
    }
    private Plant getPlantById(String plantId){
        AtomicReference<Plant> plant = new AtomicReference<>();
        db.collection("plants")
                .document(plantId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        plant.set(documentSnapshot.toObject(Plant.class));
                    }
                })
                .addOnFailureListener(e->{
                    Log.e("FireStoreGetPlantByid","Plante non récupérée : "+e.getMessage());
                })
        ;
        return plant.get();
    }


    private PlantFullService getPlantService(String plantSpecies){
        PlantFullService plantService = new PlantFullService(plantSpecies,0.0,0.0,0.0,0.0,0.0,0.0);
        db.collection("plant-service").whereEqualTo("species",plantSpecies).get()
                .addOnSuccessListener(queryDocumentSnapshots->{
                    if (!queryDocumentSnapshots.isEmpty()){
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){
                            PlantService plantService1 = documentSnapshot.toObject(PlantService.class);
                            assert plantService1 != null;
                            if (Objects.equals(plantService1.getService(), "storage_and_return_water")){
                                plantService.setValueWater(plantService1.getValue());
                                plantService.setReliabilityWater(plantService1.getReliability());
                            }
                            if (Objects.equals(plantService1.getService(), "nitrogen_provision")){
                                plantService.setValueAzote(plantService1.getValue());
                                plantService.setReliabilityAzote(plantService1.getReliability());
                            }
                            if (Objects.equals(plantService1.getService(), "soil_structuration")){
                                plantService.setValueGround(plantService1.getValue());
                                plantService.setReliabilityGround(plantService1.getReliability());
                            }
                        }

                    } else {
                        Log.d("FirestorePlantService","Aucun service trouvé "+plantSpecies);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestorePlantService","Erreur chargement bdd "+e.getMessage());
                })
        ;
        return plantService;
    }


    public void loadPlantNetListByUri(Uri plantUri){
        Map<String, Object> resultData = new HashMap<>();
        List<Plant> plantList = new ArrayList<>();
        //TODO récupérer les guess de plantNet
        // pour chacun vérifier si il existe une Plant en bdd existante
        // sinon : la créer
        ObjectMapper mapper = new ObjectMapper();
        List<PlantNetResult> results = mapper.convertValue(resultData.get("results"),
                new TypeReference<>() {
                });
        for (PlantNetResult result :results){
            db.collection("plants").whereEqualTo("gbifId",result.getGbif().getId())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()){
                            DocumentSnapshot documentSnapshot= queryDocumentSnapshots.getDocuments().get(0);
                            if (documentSnapshot.exists()){
                                Plant plant = documentSnapshot.toObject(Plant.class);
                                plantList.add(plant);
                            }
                        }
                        else { //Il n'existe pas de référence à cette plante dans la bdd -> première recherche on la crée
                            PlantFullService plantFullService = this.getPlantService(result.getSpecies().getScientificNameWithoutAuthor());
                            Plant plant = createPlant(result.getSpecies().getCommonNames().get(0),
                                    result.getSpecies().getScientificName(),
                                    result.getGbif().getId(),
                                    plantFullService.getValueAzote(),
                                    plantFullService.getReliabilityAzote(),
                                    plantFullService.getValueGround(),
                                    plantFullService.getReliabilityGround(),
                                    plantFullService.getValueWater(),
                                    plantFullService.getReliabilityWater()
                            );
                            plantList.add(plant);
                        }
                    })
                    .addOnFailureListener(e ->{});
        }
        plantNetList.setValue(plantList);
    }


    public MutableLiveData<List<Plant>> getPlantNetList() {
        return plantNetList;
    }
}
