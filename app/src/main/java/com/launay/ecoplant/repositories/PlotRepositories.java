package com.launay.ecoplant.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.launay.ecoplant.models.Plot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlotRepositories {
    private static PlotRepositories instance;
    private final MutableLiveData<List<Plot>> plotsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Plot> currentPlotLiveData = new MutableLiveData<>();

    private PlotRepositories() {

        this.loadUserPlots();
        //TODO quand firebase auth et BDD active retirer le code :

        List<Plot> plotList = new ArrayList<>();

        // Ajout des plots avec des données spécifiques
        plotList.add(new Plot("Vegetable", "Plot A", "1", "1", 48.858844f, 2.294350f, 20, 5.0, 7.3, 6.5));
        plotList.add(new Plot("Flower", "Plot B", "2", "1", 51.5074f, -0.1278f, 30, 6.1, 8.5, 7.2));
        plotList.add(new Plot("Fruit", "Plot C", "3", "1", 40.712776f, -74.005974f, 25, 7.3, 6.0, 8.1));
        plotList.add(new Plot("Vegetable", "Plot D", "4", "1", 34.052235f, -118.243683f, 40, 6.8, 7.0, 7.5));
        plotList.add(new Plot("Fruit", "Plot E", "5", "1", 37.774929f, -122.419418f, 15, 6.2, 8.0, 6.0));

        this.plotsLiveData.setValue(plotList);
        Plot plot = new Plot("Vegetable", "Plot A", "1", "1", 48.858844f, 2.294350f, 20, 5.0, 7.3, 6.5);
        this.currentPlotLiveData.setValue(plot);
    }

    public static synchronized PlotRepositories getInstance() {
        if (instance == null) {
            instance = new PlotRepositories();
        }
        return instance;
    }

    public LiveData<List<Plot>> getPlotsLiveData(){
        return this.plotsLiveData;
    }
    public MutableLiveData<Plot> getCurrentPlotLiveData(){
        return this.currentPlotLiveData;
    }

    public void loadCurrentPlot(String plotId){

        //TODO quand firebase auth et BDD active retirer le code :

        List<Plot> plotList = new ArrayList<>(this.plotsLiveData.getValue());
        for (Plot plot:plotList){
            if (Objects.equals(plot.getPlotId(), plotId)){
                this.currentPlotLiveData.setValue(plot);
            }
        }

        //TODO quand firebase auth et BDD active activer le code :

        /*
        Plot plot = null;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("plots")
                .document(plotId)
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

    public void loadUserPlots(){
        //TODO quand firebase auth et BDD active activer le code :

/*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null) {
            String userId = user.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
              .document(userId)
              .collection("plots")
              .get()
              .addOnSuccessListener(querySnapshot -> {
                  List<Plot> plots = new ArrayList<>();
                  for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                      Plot plot = doc.toObject(Plot.class);
                      plot.setPlotId(doc.getId()); // si tu veux l'id
                      plots.add(plot);
                  }
                  plotsLiveData.setValue(plots);
              })
              .addOnFailureListener(e -> {
                  plotsLiveData.setValue(Collections.emptyList());
              });
        }

 */


    }

    public void reset(){
        this.plotsLiveData.setValue(null);
        this.currentPlotLiveData.setValue(null);
    }

}
