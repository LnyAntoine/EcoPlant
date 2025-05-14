package com.launay.ecoplant.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.repositories.PlantRepositories;

import java.util.List;

public class DetectedPlantViewModel extends AndroidViewModel {
    private final LiveData<List<Plant>> plantList;
    private final PlantRepositories plantRepositories;
    public DetectedPlantViewModel(@NonNull Application application) {
        super(application);
        plantRepositories = PlantRepositories.getInstance();
        plantList = plantRepositories.getPlantsLiveData();
    }
    public LiveData<List<Plant>> getPlantList(){
        return this.plantList;
    }

    public void logout(){
        this.plantRepositories.reset();
    }
}
