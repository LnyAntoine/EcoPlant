package com.launay.ecoplant.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.models.PlantInPlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class plantRepositories {
    private final MutableLiveData<List<Plant>> plantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PlantInPlot>> plantInPlotLiveData = new MutableLiveData<>();

    public plantRepositories(){
        List<Plant> plantList = new ArrayList<>();

        // Ajout des plantes avec des données spécifiques
        plantList.add(new Plant("1", "Tomato", "Tomato Plant", "https://example.com/tomato", 7.5f, 8.0f, 6.0f));
        plantList.add(new Plant("2", "Cucumber", "Cucumber Plant", "https://example.com/cucumber", 6.0f, 7.5f, 7.0f));
        plantList.add(new Plant("3", "Carrot", "Carrot Plant", "https://example.com/carrot", 6.5f, 7.0f, 8.0f));
        plantList.add(new Plant("4", "Lettuce", "Lettuce Plant", "https://example.com/lettuce", 5.5f, 6.0f, 6.5f));
        plantList.add(new Plant("5", "Strawberry", "Strawberry Plant", "https://example.com/strawberry", 7.0f, 8.5f, 7.8f));

        plantsLiveData.setValue(plantList);
        List<PlantInPlot> plantInPlotList = new ArrayList<>();
        plantInPlotList.add(new PlantInPlot("1", "2", "1")); // Tomato in Plot B
        plantInPlotList.add(new PlantInPlot("2", "3", "1")); // Cucumber in Plot C
        plantInPlotList.add(new PlantInPlot("3", "4", "1")); // Carrot in Plot D
        plantInPlotList.add(new PlantInPlot("4", "5", "1")); // Lettuce in Plot E
        plantInPlotList.add(new PlantInPlot("5", "1", "1")); // Strawberry in Plot A

        plantInPlotList.add(new PlantInPlot("1", "3", "1")); // Tomato in Plot C
        plantInPlotList.add(new PlantInPlot("2", "4", "1")); // Cucumber in Plot D
        plantInPlotList.add(new PlantInPlot("3", "5", "1")); // Carrot in Plot E
        plantInPlotList.add(new PlantInPlot("4", "1", "1")); // Lettuce in Plot A
        plantInPlotList.add(new PlantInPlot("5", "2", "1")); // Strawberry in Plot B

        plantInPlotList.add(new PlantInPlot("1", "4", "1")); // Tomato in Plot D
        plantInPlotList.add(new PlantInPlot("2", "5", "1")); // Cucumber in Plot E
        plantInPlotList.add(new PlantInPlot("3", "1", "1")); // Carrot in Plot A
        plantInPlotList.add(new PlantInPlot("4", "2", "1")); // Lettuce in Plot B
        plantInPlotList.add(new PlantInPlot("5", "3", "1")); // Strawberry in Plot C

        plantInPlotList.add(new PlantInPlot("1", "5", "1")); // Tomato in Plot E
        plantInPlotList.add(new PlantInPlot("2", "1", "1")); // Cucumber in Plot A
        plantInPlotList.add(new PlantInPlot("3", "2", "1")); // Carrot in Plot B
        plantInPlotList.add(new PlantInPlot("4", "3", "1")); // Lettuce in Plot C
        plantInPlotList.add(new PlantInPlot("5", "4", "1")); // Strawberry in Plot D
        plantInPlotLiveData.setValue(plantInPlotList);

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
        this.plantsLiveData.setValue(plantList);
        this.plantInPlotLiveData.setValue(plantInPlots);
     */

    }

    private Plant getPlantById(String plantid){
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
                            float scoreAzote = document.getFloat("scoreAzote");
                            float scoreStruct = document.getFloat("scoreStruct");
                            float scoreWater = document.getFloat("scoreWater");


                            // Charger les informations de la plante à partir de la collection Plants
                            return new Plant(plantId,shortname,fullname,detailsLink,scoreAzote,scoreStruct,scoreWater);
                        }
                    } else {
                        // Gérer l'échec de la récupération des données
                        Log.e("PlantRepository", "Error getting documents.", task.getException());
                    }
                });
         */
        return null;
    }

    public void reset(){
        this.plantsLiveData.setValue(null);
    }


}
