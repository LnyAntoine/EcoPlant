package com.launay.ecoplant.viewmodels;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.repositories.PlantNetRepositories;

import java.util.List;

public class PlantNetViewModel extends AndroidViewModel {
    private final PlantNetRepositories plantNetRepositories;

    private final LiveData<List<Plant>> plantNetListLiveData;
    public PlantNetViewModel(@NonNull Application application) {
        super(application);
        plantNetRepositories = PlantNetRepositories.getInstance();
        plantNetListLiveData = plantNetRepositories.getPlantNetList();
    }

    public LiveData<List<Plant>> getPlantNetListLiveData() {
        return plantNetListLiveData;
    }

    public void loadPlantNetListLiveDataByUri(Context context,Uri uri){
        plantNetRepositories.loadPlantNetListByUri(context,uri);
    }

}
