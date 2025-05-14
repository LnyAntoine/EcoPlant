package com.launay.ecoplant.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.models.PlantInPlot;
import com.launay.ecoplant.repositories.PlantRepositories;

import java.util.List;

public class PlantInPlotViewModel extends AndroidViewModel {
    private final PlantRepositories plantRepositories;
    private final LiveData<List<Plant>> plantlistLiveData;
    private final LiveData<List<PlantInPlot>> plantInPlotListLiveData;

    public PlantInPlotViewModel(@NonNull Application application) {
        super(application);
        plantRepositories = PlantRepositories.getInstance();
        plantlistLiveData = plantRepositories.getPlantsLiveData();
        plantInPlotListLiveData = plantRepositories.getPlantInPlotLiveData();
    }
    public LiveData<List<Plant>> getPlantlistLiveData(){
        return this.plantlistLiveData;
    }
    public LiveData<List<PlantInPlot>> getPlantInPlotListLiveData(){
        return this.plantInPlotListLiveData;
    }
    public void loadPlotPlants(String plotid){
        this.plantRepositories.loadPlantForPlot(plotid);
    }
}
