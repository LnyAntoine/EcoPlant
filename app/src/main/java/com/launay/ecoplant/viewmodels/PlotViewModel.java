package com.launay.ecoplant.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.launay.ecoplant.models.Plot;
import com.launay.ecoplant.repositories.PlotRepositories;
import com.launay.ecoplant.utils;

import java.util.List;

public class PlotViewModel extends AndroidViewModel {
    private final LiveData<List<Plot>> plotsLiveData;
    private final LiveData<Plot> currentPlotLiveData;
    private final PlotRepositories plotRepositories;
    public PlotViewModel(@NonNull Application application) {
        super(application);
        this.plotRepositories = PlotRepositories.getInstance();
        plotsLiveData = plotRepositories.getPlotsLiveData();
        currentPlotLiveData = plotRepositories.getCurrentPlotLiveData();
    }


    public void createPlot(String name){
        this.plotRepositories.createPlot(name,0.,0.,"private");
    }
    public LiveData<Plot> getPlotById(){return this.plotRepositories.getPlotById();}
    public void loadPlotByid(String id){plotRepositories.loadPlotById(id);}
    public LiveData<Plot> getCurrentPlotLiveData(){return this.currentPlotLiveData;}
    public LiveData<List<Plot>> getPlotsLiveData(){return this.plotsLiveData;}
    public void loadPlots(){
        plotRepositories.loadUserPlots();
    }
    public void loadCurrentPlot(String id){
        plotRepositories.loadCurrentPlot(id);
    }
    public void deletePlot(String plotid, utils.AuthCallback authCallback){
        plotRepositories.deletePlot(plotid,authCallback);
    }
    public void logout(){
        plotRepositories.reset();
    }
}
