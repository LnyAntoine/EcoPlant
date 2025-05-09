package com.launay.ecoplant.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.launay.ecoplant.models.Plot;

import java.util.ArrayList;
import java.util.List;

public class plotRepositories {
    private final MutableLiveData<List<Plot>> plotsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Plot> currentPlotLiveData = new MutableLiveData<>();

    public plotRepositories() {
        List<Plot> plotList = new ArrayList<>();

        // Ajout des plots avec des données spécifiques
        plotList.add(new Plot("Vegetable", "Plot A", "1", "1", 48.858844f, 2.294350f, 20, 5.0f, 7.3f, 6.5f));
        plotList.add(new Plot("Flower", "Plot B", "2", "1", 51.5074f, -0.1278f, 30, 6.1f, 8.5f, 7.2f));
        plotList.add(new Plot("Fruit", "Plot C", "3", "1", 40.712776f, -74.005974f, 25, 7.3f, 6.0f, 8.1f));
        plotList.add(new Plot("Vegetable", "Plot D", "4", "1", 34.052235f, -118.243683f, 40, 6.8f, 7.0f, 7.5f));
        plotList.add(new Plot("Fruit", "Plot E", "5", "1", 37.774929f, -122.419418f, 15, 6.2f, 8.0f, 6.0f));

        this.plotsLiveData.setValue(plotList);
        Plot plot = new Plot("Vegetable", "Plot A", "1", "1", 48.858844f, 2.294350f, 20, 5.0f, 7.3f, 6.5f);
        this.currentPlotLiveData.setValue(plot);
    }

    public LiveData<List<Plot>> getPlotsLiveData(){
        return this.plotsLiveData;
    }
    public LiveData<Plot> getCurrentPlotLiveData(){
        return this.currentPlotLiveData;
    }

    public void loadCurrentPlot(String plotId){
        /*
        Plot plot = null;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("plots")
                .document(userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        plot = doc.toObject(Plot.class);
                        plot.setId(doc.getId()); // si tu veux l'id

                    }
                    currentPlotLiveData.setValue(plot);
                })
                .addOnFailureListener(e -> {
                    currentPlotLiveData.postValue(null);
                });

         */
    }

    public void loadUserPlots(String userId){
        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
          .document(userId)
          .collection("plots")
          .get()
          .addOnSuccessListener(querySnapshot -> {
              List<Plot> plots = new ArrayList<>();
              for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                  Plot plot = doc.toObject(Plot.class);
                  plot.setId(doc.getId()); // si tu veux l'id
                  plots.add(plot);
              }
              plotListLiveData.postValue(plots);
          })
          .addOnFailureListener(e -> {
              plotListLiveData.postValue(Collections.emptyList());
          });
         */

    }

    public void reset(){
        this.plotsLiveData.setValue(null);
    }

}
