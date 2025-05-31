package com.launay.ecoplant.viewmodels;

import android.app.Application;
import android.location.Location;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.launay.ecoplant.models.Observation;
import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.repositories.ObservationRepositories;

import java.util.List;
import java.util.function.Consumer;

public class ObservationViewModel extends AndroidViewModel {
    private ObservationRepositories observationRepositories;
    private LiveData<List<Observation>> observationListLiveData;
    private LiveData<Observation> currentObservationLiveData;
    private MutableLiveData<Uri> obsUriLiveData;
    private MutableLiveData<Location> observationLocationLiveData;




    public ObservationViewModel(@NonNull Application application) {
        super(application);
        observationRepositories = ObservationRepositories.getInstance();
        observationListLiveData = observationRepositories.getObservationList();
        currentObservationLiveData = observationRepositories.getCurrentObservation();
        obsUriLiveData = new MutableLiveData<>();
        observationLocationLiveData = new MutableLiveData<>();
    }

    public LiveData<Uri> getObsUriLiveData(){
        return obsUriLiveData;
    }
    public void setObsUriLiveData(Uri obsUri){
        obsUriLiveData.setValue(obsUri);
    }

    public MutableLiveData<Location> getObservationLocationLiveData() {
        return observationLocationLiveData;
    }

    public void setObservationLocationLiveData(Location location) {
        this.observationLocationLiveData.setValue(location);
    }


    public void createObservation(Plant plant, String plotId, Uri obsUri,int nbPlantes){
        observationRepositories.createObservation(plant,plotId,obsUri,observationLocationLiveData.getValue(),nbPlantes);
    }
    public void deleteObservationById(String plotId, String obsId, Consumer<Boolean> callback){
        observationRepositories.deleteObservationById(plotId,obsId,callback);
    }

    public void loadObservationListLiveDataByPlotId(String plotId){
        observationRepositories.loadObservationListByPlotId(plotId);
    }
    public void loadCurrentObservationLiveDataById(String plotId,String observationId){
        observationRepositories.loadObservationById(plotId,observationId);
    }

    public LiveData<List<Observation>> getObservationListLiveData() {
        return observationListLiveData;
    }

    public LiveData<Observation> getCurrentObservationLiveData() {
        return currentObservationLiveData;
    }
    public void refreshData(String plotId){
        observationRepositories.refreshData(plotId);
    }

}
