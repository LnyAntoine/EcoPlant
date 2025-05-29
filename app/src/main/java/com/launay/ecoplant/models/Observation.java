package com.launay.ecoplant.models;

import java.util.Date;

public class Observation {

    private String observationId;
    private String observerId;
    private String plantId;
    private Date date;
    private String plotId;
    private String notes;

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    private Plant plant;

    public Observation(String observationId,String observerId, String plantId, Date date, String plotId, String notes,Plant plant) {
        this.observationId = observationId;
        this.observerId = observerId;
        this.plantId = plantId;
        this.date = date;
        this.plotId = plotId;
        this.notes = notes;
        this.plant = plant;
    }

    public Observation() {
    }

    public String getObserverId() {
        return observerId;
    }

    public void setObserverId(String observerId) {
        this.observerId = observerId;
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
                "observerId='" + observerId + '\'' +
                ", plantId='" + plantId + '\'' +
                ", date=" + date +
                ", plotId='" + plotId + '\'' +
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

}
