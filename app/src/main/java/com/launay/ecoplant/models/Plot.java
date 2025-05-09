package com.launay.ecoplant.models;

public class Plot {
    String type;
    String name;
    String plotId;
    String pictureUri;
    String ownerId;
    Float latitude;
    Float longitude;
    int nbPlant;
    float scoreAzote;
    float scoreStruct;
    float scoreWater;

    public Plot(String type, String name, String plotId, String ownerId, Float latitude, Float longitude, int nbPlant, float scoreAzote, float scoreStruct, float scoreWater) {
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

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public int getNbPlant() {
        return nbPlant;
    }

    public float getScoreAzote() {
        return scoreAzote;
    }

    public float getScoreStruct() {
        return scoreStruct;
    }

    public float getScoreWater() {
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

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setNbPlant(int nbPlant) {
        this.nbPlant = nbPlant;
    }

    public void setScoreAzote(float scoreAzote) {
        this.scoreAzote = scoreAzote;
    }

    public void setScoreStruct(float scoreStruct) {
        this.scoreStruct = scoreStruct;
    }

    public void setScoreWater(float scoreWater) {
        this.scoreWater = scoreWater;
    }
}
