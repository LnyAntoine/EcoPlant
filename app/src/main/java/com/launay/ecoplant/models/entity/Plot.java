package com.launay.ecoplant.models.entity;

public class Plot {
    Boolean publicP;
    String name;
    String plotId;
    String pictureUrl;
    String ownerId;
    Double latitude;

    @Override
    public String toString() {
        return "Plot{" +
                "publicP=" + publicP +
                ", name='" + name + '\'' +
                ", plotId='" + plotId + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", nbPlant=" + nbPlant +
                '}';
    }

    Double longitude;
    int nbPlant;


    public Plot(){}

    public Plot(Boolean publicP, String name, String plotId, String ownerId, Double latitude, Double longitude, int nbPlant) {
        this.publicP = publicP;
        this.name = name;
        this.plotId = plotId;
        this.ownerId = ownerId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nbPlant = nbPlant;

    }

    public Boolean getPublicP() {
        return publicP;
    }

    public String getName() {
        return name;
    }

    public String getPlotId() {
        return plotId;
    }

    public String getPictureUrl() {
        return pictureUrl;
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



    public void setType(Boolean publicP) {
        this.publicP = publicP;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlotId(String plotId) {
        this.plotId = plotId;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
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


}
