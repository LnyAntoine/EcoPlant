package com.launay.ecoplant.models;

public class PlantInPlot {
    String plantId;
    String plotId;
    String pictureURI;
    String userId;

    public PlantInPlot(String plantId, String plotId, String userId) {
        this.plantId = plantId;
        this.plotId = plotId;
        this.userId = userId;
    }

    public String getPlantId() {
        return plantId;
    }

    public String getPlotId() {
        return plotId;
    }

    public String getPictureURI() {
        return pictureURI;
    }

    public String getUserId() {
        return userId;
    }
}
