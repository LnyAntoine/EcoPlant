package com.launay.ecoplant.models;

import com.google.firebase.database.Exclude;

public class Plant {
    String plantId;
    String shortname;
    String fullname;
    String detailsLink;

    String gbifId;

    String powoId;
    String pictureUrl;
    Double scoreAzote;
    Double scoreStruct;
    double reliabilityWater;
    double reliabilityGround;
    double reliabilityAzote;
    @Exclude
    double score;



    @Exclude
    int nbplant;

    Double scoreWater;

    public Plant(){}

    public Plant(String plantId, String shortname, String fullname,String powoId, String gbifId, Double scoreAzote, Double scoreStruct, double reliabilityWater, double reliabilityGround, double reliabilityAzote, Double scoreWater) {
        this.plantId = plantId;
        this.shortname = shortname;
        this.fullname = fullname;
        this.powoId = powoId;
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

    public void setScore(Double score){this.score=score;}
    public Double getScore(){return this.score;}
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

    public String getPictureUrl() {
        return pictureUrl;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "plantId='" + plantId + '\'' +
                ", shortname='" + shortname + '\'' +
                ", fullname='" + fullname + '\'' +
                ", detailsLink='" + detailsLink + '\'' +
                ", powoId='" + powoId + '\'' +
                ", gbifId='" + gbifId + '\'' +
                ", pictureURl='" + pictureUrl + '\'' +
                ", scoreAzote=" + scoreAzote +
                ", scoreStruct=" + scoreStruct +
                ", scoreWater=" + scoreWater +
                '}';
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setDetailsLink(String detailsLink) {
        this.detailsLink = detailsLink;
    }

    public void setGbifId(String gbifId) {
        this.gbifId = gbifId;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setScoreAzote(Double scoreAzote) {
        this.scoreAzote = scoreAzote;
    }

    public void setScoreStruct(Double scoreStruct) {
        this.scoreStruct = scoreStruct;
    }

    public double getReliabilityWater() {
        return reliabilityWater;
    }

    public void setReliabilityWater(double reliabilityWater) {
        this.reliabilityWater = reliabilityWater;
    }

    public double getReliabilityGround() {
        return reliabilityGround;
    }

    public void setReliabilityGround(double reliabilityGround) {
        this.reliabilityGround = reliabilityGround;
    }

    public double getReliabilityAzote() {
        return reliabilityAzote;
    }

    public void setReliabilityAzote(double reliabilityAzote) {
        this.reliabilityAzote = reliabilityAzote;
    }

    public void setScoreWater(Double scoreWater) {
        this.scoreWater = scoreWater;
    }

    public String getPowoId() {
        return powoId;
    }

    public void setPowoId(String powoId) {
        this.powoId = powoId;
    }
    public int getNbplant() {
        return nbplant;
    }

    public void setNbplant(int nbplant) {
        this.nbplant = nbplant;
    }


}
