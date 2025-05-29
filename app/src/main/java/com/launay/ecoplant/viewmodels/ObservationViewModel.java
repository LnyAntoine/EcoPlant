package com.launay.ecoplant.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.launay.ecoplant.models.Observation;
import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.repositories.ObservationRepositories;

import java.util.List;

public class ObservationViewModel extends ViewModel{
    private ObservationRepositories observationRepositories;
    private LiveData<List<Observation>> observationListLiveData;
    private LiveData<Observation> currentObservationLiveData;
    public ObservationViewModel(@NonNull Application application) {
        super(application);
        observationRepositories = ObservationRepositories.getInstance();
        observationListLiveData = observationRepositories.getObservationList();
        currentObservationLiveData = observationRepositories.getCurrentObservation();
    }

    public void createObservation(Plant plant,String plotId){
        observationRepositories.createObservation(plant,plotId);
    }

    public void loadObservationListLiveDataByPlotId(String plotId){
        observationRepositories.loadObservationListByPlotId(plotId);
    }
    public void loadCurrentObservationLiveDataById(String observationId){
        observationRepositories.loadObservationById(observationId);
    }

    public LiveData<List<Observation>> getObservationListLiveData() {
        return observationListLiveData;
    }

    public LiveData<Observation> getCurrentObservationLiveData() {
        return currentObservationLiveData;
    }

}
