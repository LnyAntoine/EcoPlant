package com.launay.ecoplant.repositories;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.models.PlantNetResult;
import com.launay.ecoplant.models.PlantFullService;
import com.launay.ecoplant.models.PlantService;
import com.launay.ecoplant.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PlantNetRepositories {
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
    }

    public void createPlant(String shortname, String fullname,String powoId ,String gbifId,
                            Double scoreAzote, Double scoreStruct, double reliabilityWater,
                            double reliabilityGround, double reliabilityAzote, Double scoreWater,
                             Consumer<Plant> callback) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            String pictureUrl = "";
            String url = "https://api.gbif.org/v1/occurrence/search?taxon_key=" + gbifId + "&mediaType=StillImage";

            try {
                JSONArray results = getJsonArray(url); // ← doit être thread-safe
                if (results.length() > 0) {
                    JSONObject first = results.getJSONObject(0);
                    JSONArray media = first.getJSONArray("media");
                    if (media.length() > 0) {
                        pictureUrl = media.getJSONObject(0).getString("identifier");
                    }
                }
            } catch (Exception e) {
                Log.e("FirebasePlantNet", "Erreur lors de la récuperation de l'image via gbif : " + e);
                /*try {
                    url = "https://api.gbif.org/v1/occurrence/search?taxon_key=" + powoId + "&mediaType=StillImage";
                    JSONArray results = getJsonArray(url); // ← doit être thread-safe
                    if (results.length() > 0) {
                        JSONObject first = results.getJSONObject(0);
                        JSONArray media = first.getJSONArray("media");
                        if (media.length() > 0) {
                            pictureUrl = media.getJSONObject(0).getString("identifier");
                        }
                    }
                } catch (Exception e1) {*/
                    Log.e("FirebasePlantNet", "Erreur lors de la récuperation de l'image via powo: " + e);

                //}
            }

            String finalPictureUrl = pictureUrl;
            handler.post(() -> {
                // Création de la plante dans le thread principal
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Plant plant = new Plant(
                        null,
                        shortname,
                        fullname,
                        powoId,
                        gbifId,
                        scoreAzote,
                        reliabilityAzote,
                        scoreStruct,
                        reliabilityGround,
                        scoreWater,
                        reliabilityWater
                );
                plant.setPictureUrl(finalPictureUrl);

                db.collection("plants")
                        .add(plant)
                        .addOnSuccessListener(documentReference -> {
                            getPlantById(documentReference.getId(),
                                    plant2 -> {
                                            plant2.setPlantId(documentReference.getId());
                                            callback.accept(plant2); // ✅ Retour ici
                            });

                        })
                        .addOnFailureListener(e -> {
                            Log.e("FireStorePlant", "Erreur création plante : " + e.getMessage());
                            callback.accept(null); // En cas d'erreur
                        });
            });
        });
    }

    private void getPlantById(String plantId,Consumer<Plant> callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (plantId.isEmpty()){
            callback.accept(null);
        }
        db.collection("plants")
                .document(plantId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        Plant plant = documentSnapshot.toObject(Plant.class);
                        assert plant != null;
                        plant.setPlantId(plantId);
                        Log.e("FireStoreGetPlantByid","Plante récupérée : "+plant);
                        callback.accept(plant);
                    }
                })
                .addOnFailureListener(e->{
                    Log.e("FireStoreGetPlantByid","Plante non récupérée : "+e.getMessage());
                    callback.accept(null);
                })
        ;

    }

    private PlantFullService getPlantService(String plantSpecies){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (plantSpecies.isEmpty()){
            return null;
        }
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


    public void loadPlantNetListByUri(Uri plantUri) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d("LoadPlantNetListByUri", "loading");
        Map<String, Object> resultData = new HashMap<>();
        List<Plant> plantList = new ArrayList<>();
        //TODO récupérer les guess de plantNet
        // pour chacun vérifier si il existe une Plant en bdd existante
        // sinon : la créer


        ObjectMapper mapper = new ObjectMapper();
        List<PlantNetResult> results = mapper.convertValue(resultData.get("results"),
                new TypeReference<>() {
                });

        String jsonresponse = utils.requestAPIPlantNet();

        try {
            // Convertir la chaîne JSON en Map
            Map<String, Object> jsonMap = mapper.readValue(jsonresponse, new TypeReference<>() {
            });

            // Extraire et convertir la liste des résultats
            results = mapper.convertValue(
                    jsonMap.get("results"),
                    new TypeReference<>() {
                    }
            );
        } catch (JsonProcessingException e) {
            Log.e("Erreur", " " + e.getMessage());
        }


        AtomicInteger completedRequests = new AtomicInteger(0);
        int total = results.size();

        for (PlantNetResult result : results) {
            String field;
            String search;

            if (result.getPowo()==null && result.getGbif() == null) {
                if (completedRequests.incrementAndGet() == total) {
                    plantNetList.setValue(plantList);
                }
            } else {
                if (result.getGbif() != null) {
                    field = "gbifId";
                    search = result.getGbif().getId();
                } else {
                    field = "powoId";
                    search = result.getPowo().getId();
                }
                db.collection("plants").whereEqualTo(field, search)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                if (documentSnapshot.exists()) {
                                    Plant plant = documentSnapshot.toObject(Plant.class);
                                    plantList.add(plant);

                                }
                                if (completedRequests.incrementAndGet() == total) {
                                    Log.d("LoadPlantNetListByUri", "end " + plantList);
                                    plantNetList.setValue(plantList);
                                }
                            } else {
                                String powoId = (result.getPowo() != null) ? result.getPowo().getId() : "";
                                String gbifId = (result.getGbif() != null) ? result.getGbif().getId() : "";
                                PlantFullService plantFullService = this.getPlantService(result.getSpecies().getScientificNameWithoutAuthor());
                                createPlant(
                                        result.getSpecies().getCommonNames().get(0),
                                        result.getSpecies().getScientificName(),
                                        powoId,
                                        gbifId,
                                        plantFullService.getValueAzote(),
                                        plantFullService.getReliabilityAzote(),
                                        plantFullService.getValueGround(),
                                        plantFullService.getReliabilityGround(),
                                        plantFullService.getValueWater(),
                                        plantFullService.getReliabilityWater(),
                                        plant -> {
                                            if (plant != null) {
                                                plant.setDetailsLink((result.getPowo() != null) ? result.getPowo().getUrl() :
                                                        (result.getGbif() != null) ? result.getGbif().getUrl() : "https://cinepulse.to/sheet/movie-843");
                                                plantList.add(plant);
                                            }
                                            if (completedRequests.incrementAndGet() == total) {
                                                Log.d("LoadPlantNetListByUri", "end " + plantList);
                                                plantNetList.setValue(plantList);
                                            }
                                        }
                                );

                            }


                        })
                        .addOnFailureListener(e -> {
                            Log.e("loadPlant", " " + e.getMessage());

                            // On compte aussi les erreurs comme complétées
                            if (completedRequests.incrementAndGet() == total) {
                                plantNetList.setValue(plantList);
                            }
                        });
            }
        }
    }

    @NonNull
    private static JSONArray getJsonArray(String url) throws IOException, JSONException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(response.toString());
        return json.getJSONArray("results");
    }


    public MutableLiveData<List<Plant>> getPlantNetList() {
        return plantNetList;
    }
}
