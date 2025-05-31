package com.launay.ecoplant.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.models.PlantInPlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PlantRepositories {
    private final MutableLiveData<List<Plant>> plantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PlantInPlot>> plantInPlotLiveData = new MutableLiveData<>();



    private final MutableLiveData<List<Plant>> detectedPlantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PlantInPlot>> detectedPlantIntPlotLiveData = new MutableLiveData<>();
    private static PlantRepositories instance;

    public static PlantRepositories getInstance(){
        if (instance==null){
            instance = new PlantRepositories();
        }
        return instance;
    }

    private PlantRepositories(){

        //TODO quand firebase auth et BDD active retirer le code :

        List<Plant> plantList = new ArrayList<>();

        plantsLiveData.setValue(plantList);

        plantInPlotLiveData.setValue(new ArrayList<>());

    }
    public MutableLiveData<List<PlantInPlot>> getDetectedPlantIntPlotLiveData() {
        return detectedPlantIntPlotLiveData;
    }
    public MutableLiveData<List<Plant>> getDetectedPlantsLiveData() {
        return detectedPlantsLiveData;
    }
    public LiveData<List<Plant>> getPlantsLiveData(){
        return this.plantsLiveData;
    }

    public LiveData<List<PlantInPlot>> getPlantInPlotLiveData(){
        return this.plantInPlotLiveData;
    }
    public void loadPlantForPlot(String plotId){

        List<Plant> plantList = new ArrayList<>();
        List<PlantInPlot> plantInPlots = new ArrayList<>();
        Map<String,Integer> plantMap = new HashMap<>();


        //TODO quand firebase auth et BDD active activer le code :
        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("PlantInPlot")
            .whereEqualTo("plotId", plotId) // Filtre par plotId
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    // Pour chaque document trouvé, récupérer le plantId
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String plantId = document.getString("plantId");
                        String userId = document.getString("userId"); // L'utilisateur qui a ajouté la plante

                        // Charger les informations de la plante à partir de la collection Plants
                        plantInPlots.add(new PlantInPlot(plantId,plotId,userId));

                        if (plantMap.containsKey(plantId)){
                            plantMap.replace(plantId,plantMap.get(plantId)+1);
                        } else{
                            plantMap.put(plantId,1);
                        }
                    }
                    for (String plantId : plantMap.keySet()) {
                        plantList.add(this.getPlantById(plantId));
                    }

                } else {
                    // Gérer l'échec de la récupération des données
                    Log.e("PlantRepository", "Error getting documents.", task.getException());
                }
            });


         */
        this.plantsLiveData.setValue(plantList);
        this.plantInPlotLiveData.setValue(plantInPlots);


    }

    private Plant getPlantById(String plantid){
        AtomicReference<Plant> plant = new AtomicReference<>();
        //TODO quand firebase auth et BDD active activer le code :
        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Plant")
                .whereEqualTo("plantId", plantid) // Filtre par plotId
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String plantId = document.getString("plantId");
                            String shortname = document.getString("shortname");
                            String fullname = document.getString("fullname");
                            String detailsLink = document.getString("detailsLink");
                            Double scoreAzote = document.getDouble("scoreAzote");
                            Double scoreStruct = document.getDouble("scoreStruct");
                            Double scoreWater = document.getDouble("scoreWater");
                            // Charger les informations de la plante à partir de la collection Plants
                            plant.set(new Plant(plantId, shortname, fullname, detailsLink, scoreAzote, scoreStruct, scoreWater));
                        }
                    } else {
                        // Gérer l'échec de la récupération des données
                        Log.e("PlantRepository", "Error getting documents.", task.getException());

                    }
                });
        */
        return plant.get();
    }


    private void detectPlant(String URIphoto,String plotId){
        //TODO a faire
    }

    public void reset(){
        this.plantsLiveData.setValue(null);
    }


}
