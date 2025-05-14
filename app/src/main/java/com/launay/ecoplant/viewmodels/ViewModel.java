package com.launay.ecoplant.viewmodels;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.launay.ecoplant.models.Plant;
import com.launay.ecoplant.models.PlantInPlot;
import com.launay.ecoplant.models.Plot;
import com.launay.ecoplant.models.User;
import com.launay.ecoplant.repositories.UserRepositories;
import com.launay.ecoplant.repositories.PlotRepositories;
import com.launay.ecoplant.repositories.PlantRepositories;

import java.util.List;

public class ViewModel extends AndroidViewModel {


    private final UserRepositories userRepositories;
    private final PlotRepositories plotRepositories;
    private final PlantRepositories plantRepositories;
    private final LiveData<User> userLiveData;
    private final LiveData<List<Plot>> plotsLiveData;
    private final LiveData<Plot> currentPlotLiveData;

    private final LiveData<List<Plant>> plantsLiveData;
    private final LiveData<List<PlantInPlot>> plantsInPlotLiveData;
    private final MutableLiveData<Pair<List<Plant>,List<PlantInPlot>>> combinedLiveData;

    public ViewModel(@NonNull Application application) {
        super(application);
        this.userRepositories = UserRepositories.getInstance();
        this.plotRepositories = PlotRepositories.getInstance();
        this.plantRepositories = PlantRepositories.getInstance();

        this.userLiveData = userRepositories.getUserLiveData();
        userRepositories.loadCurrentUser();
        this.plotsLiveData = this.plotRepositories.getPlotsLiveData();
        this.plantsLiveData = this.plantRepositories.getPlantsLiveData();
        this.plantsInPlotLiveData = this.plantRepositories.getPlantInPlotLiveData();
        this.currentPlotLiveData = this.plotRepositories.getCurrentPlotLiveData();
        this.combinedLiveData = new MutableLiveData<>();
        this.combinedLiveData.setValue(new Pair<>(this.plantsLiveData.getValue(),this.plantsInPlotLiveData.getValue()));

    }

    public LiveData<Plot> getCurrentPlotLiveData(){
        return this.currentPlotLiveData;
    }
    public LiveData<List<Plot>> getPlotsLiveData(){
        return this.plotsLiveData;
    }
    public LiveData<User> getUserLiveData(){
        return this.userLiveData;
    }

    public LiveData<List<Plant>> getPlantsLiveData() {
        return plantsLiveData;
    }

    public LiveData<Pair<List<Plant>,List<PlantInPlot>>> getCombinedLiveData(){
        return this.combinedLiveData;
    }

    public void refreshPlants(){
        this.plantRepositories.loadPlantForPlot(this.currentPlotLiveData.getValue().getPlotId());
        this.combinedLiveData.setValue(new Pair<>(this.plantsLiveData.getValue(),this.plantsInPlotLiveData.getValue()));
    }
    public void refreshCurrentPlot(String plotId){
        this.plotRepositories.loadCurrentPlot(plotId);
    }
    public void refreshPlots(){
        this.plotRepositories.loadUserPlots();
    }
    public void refreshUser(){
        this.userRepositories.loadCurrentUser();;
    }
    public void logout(){
        this.userRepositories.logout();
        this.plotRepositories.reset();
        this.plantRepositories.reset();
    }
}
