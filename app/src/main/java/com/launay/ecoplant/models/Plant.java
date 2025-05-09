package com.launay.ecoplant.models;

public class Plant {
    String plantId;
    String shortname;
    String fullname;
    String detailsLink;
    String pictureURI;
    float scoreAzote;
    float scoreStruct;
    float scoreWater;

    public Plant(String plantId, String shortname, String fullname, String detailsLink, float scoreAzote, float scoreStruct, float scoreWater) {
        this.plantId = plantId;
        this.shortname = shortname;
        this.fullname = fullname;
        this.detailsLink = detailsLink;
        this.scoreAzote = scoreAzote;
        this.scoreStruct = scoreStruct;
        this.scoreWater = scoreWater;
    }
}
