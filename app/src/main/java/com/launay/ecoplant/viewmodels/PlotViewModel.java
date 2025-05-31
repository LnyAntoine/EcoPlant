package com.launay.ecoplant.viewmodels;

import android.app.Application;
import android.location.Location;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.launay.ecoplant.models.Plot;
import com.launay.ecoplant.repositories.PlotRepositories;
import com.launay.ecoplant.utils;

import java.util.List;
import java.util.function.Consumer;

public class PlotViewModel extends AndroidViewModel {
    private final LiveData<List<Plot>> plotsLiveData;
    private final LiveData<Plot> currentPlotLiveData;
    private final PlotRepositories plotRepositories;
    private MutableLiveData<Location> plotLocationLiveData;
    public PlotViewModel(@NonNull Application application) {
        super(application);
        this.plotRepositories = PlotRepositories.getInstance();
        plotsLiveData = plotRepositories.getPlotsLiveData();
        currentPlotLiveData = plotRepositories.getCurrentPlotLiveData();
        plotLocationLiveData = new MutableLiveData<>();
        plotLocationLiveData.setValue(null);
    }

    public LiveData<Location> getPlotLocationLiveData(){
        return plotLocationLiveData;
    }
    public void setPlotLocationLiveData(Location location){
        plotLocationLiveData.setValue(location);
    }
    public void createPlot(String name, Uri pictureUri, double lat, double longi, Boolean publicP, Consumer<String > callback){
        this.plotRepositories.createPlot(name,pictureUri,lat,longi,publicP,callback);
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
    public void deletePlot(String plotid, Consumer<Boolean> callback){
        plotRepositories.deletePlot(plotid,callback);
    }
    public void refreshData(){
        plotRepositories.refreshData();
    }
    public void logout(){
        plotRepositories.reset();
    }
}
