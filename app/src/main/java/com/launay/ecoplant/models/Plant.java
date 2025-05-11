package com.launay.ecoplant.models;

public class Plant {
    String plantId;
    String shortname;
    String fullname;
    String detailsLink;
    String pictureURI;
    Double scoreAzote;
    Double scoreStruct;



    Double scoreWater;

    public Plant(String plantId, String shortname, String fullname, String detailsLink, Double scoreAzote, Double scoreStruct, Double scoreWater) {
        this.plantId = plantId;
        this.shortname = shortname;
        this.fullname = fullname;
        this.detailsLink = detailsLink;
        this.scoreAzote = scoreAzote;
        this.scoreStruct = scoreStruct;
        this.scoreWater = scoreWater;
    }

    public String getPlantId() {
        return plantId;
    }

    public String getShortname() {
        return shortname;
    }

    public String getFullname() {
        return fullname;
    }

    public String getDetailsLink() {
        return detailsLink;
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
}
