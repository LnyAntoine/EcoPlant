package com.launay.ecoplant.models;

public class Plot {
    String type;
    String name;
    String plotId;
    String pictureUri;
    String ownerId;
    Double latitude;
    Double longitude;
    int nbPlant;
    Double scoreAzote;
    Double scoreStruct;
    Double scoreWater;

    public Plot(){}

    public Plot(String type, String name, String plotId, String ownerId, Double latitude, Double longitude, int nbPlant, Double scoreAzote, Double scoreStruct, Double scoreWater) {
        this.type = type;
        this.name = name;
        this.plotId = plotId;
        this.ownerId = ownerId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nbPlant = nbPlant;
        this.scoreAzote = scoreAzote;
        this.scoreStruct = scoreStruct;
        this.scoreWater = scoreWater;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPlotId() {
        return plotId;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public int getNbPlant() {
        return nbPlant;
    }

    public Double getScoreAzote() {
        return scoreAzote;
    }

    public Double getScoreStruct() {
        return scoreStruct;
    }

    public Double getScoreWater() {
        return scoreWater;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlotId(String plotId) {
        this.plotId = plotId;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setNbPlant(int nbPlant) {
        this.nbPlant = nbPlant;
    }

    public void setScoreAzote(Double scoreAzote) {
        this.scoreAzote = scoreAzote;
    }

    public void setScoreStruct(Double scoreStruct) {
        this.scoreStruct = scoreStruct;
    }

    public void setScoreWater(Double scoreWater) {
        this.scoreWater = scoreWater;
    }
}
