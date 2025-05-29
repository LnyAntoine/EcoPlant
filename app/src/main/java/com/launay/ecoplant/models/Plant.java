package com.launay.ecoplant.models;

public class Plant {
    String plantId;
    String shortname;
    String fullname;
    String detailsLink;

    String gbifId;
    String pictureURI;
    Double scoreAzote;
    Double scoreStruct;
    double reliabilityWater;
    double reliabilityGround;
    double reliabilityAzote;



    Double scoreWater;

    public Plant(){}

    public Plant(String plantId, String shortname, String fullname, String gbifId, Double scoreAzote, Double scoreStruct, Double scoreWater) {
        this.plantId = plantId;
        this.shortname = shortname;
        this.fullname = fullname;
        this.gbifId = gbifId;
        this.scoreAzote = scoreAzote;
        this.scoreStruct = scoreStruct;
        this.scoreWater = scoreWater;
    }

    public Plant(String plantId, String shortname, String fullname, String gbifId, Double scoreAzote, Double scoreStruct, double reliabilityWater, double reliabilityGround, double reliabilityAzote, Double scoreWater) {
        this.plantId = plantId;
        this.shortname = shortname;
        this.fullname = fullname;
        this.gbifId = gbifId;
        this.scoreAzote = scoreAzote;
        this.scoreStruct = scoreStruct;
        this.reliabilityWater = reliabilityWater;
        this.reliabilityGround = reliabilityGround;
        this.reliabilityAzote = reliabilityAzote;
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
    public String getGbifId() {
        return gbifId;
    }

    public String getPictureURI() {
        return pictureURI;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "plantId='" + plantId + '\'' +
                ", shortname='" + shortname + '\'' +
                ", fullname='" + fullname + '\'' +
                ", detailsLink='" + detailsLink + '\'' +
                ", gbifId='" + gbifId + '\'' +
                ", pictureURI='" + pictureURI + '\'' +
                ", scoreAzote=" + scoreAzote +
                ", scoreStruct=" + scoreStruct +
                ", scoreWater=" + scoreWater +
                '}';
    }
}
