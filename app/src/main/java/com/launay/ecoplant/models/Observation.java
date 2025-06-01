package com.launay.ecoplant.models;

import java.util.Date;

public class Observation {

    private String observationId;
    private String userId;

    private Double lat;

    private Double longi;

    private String pictureUrl;
    private String plantId;
    private Date date;
    private String plotId;
    private String notes;


    private int nbPlantes;


    private Plant plant;

    public Observation(String observationId,String userId, String plantId, Date date, String plotId, String notes,Plant plant,String pictureUrl,int nbPlantes) {
        this.nbPlantes = nbPlantes;
        this.observationId = observationId;
        this.userId = userId;
        this.plantId = plantId;
        this.date = date;
        this.plotId = plotId;
        this.notes = notes;
        this.plant = plant;
        this.pictureUrl = pictureUrl;
    }
    public Observation(String observationId,String observerId, String plantId, Date date, String plotId, String notes,Plant plant,int nbPlantes) {
        this.observationId = observationId;
        this.userId = observerId;
        this.plantId = plantId;
        this.date = date;
        this.plotId = plotId;
        this.notes = notes;
        this.plant = plant;
        this.nbPlantes = nbPlantes;
    }

    public Observation() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlotId() {
        return plotId;
    }

    public void setPlotId(String plotId) {
        this.plotId = plotId;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "userId='" + userId + '\'' +
                ", plantId='" + plantId + '\'' +
                ", date=" + date +
                ", plotId='" + plotId + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                '}';
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(String observationId) {
        this.observationId = observationId;
    }


    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public int getNbPlantes() {
        return nbPlantes;
    }

    public void setNbPlantes(int nbPlantes) {
        this.nbPlantes = nbPlantes;
    }


}
