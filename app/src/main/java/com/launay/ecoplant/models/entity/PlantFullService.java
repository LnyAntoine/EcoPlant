package com.launay.ecoplant.models.entity;

public class PlantFullService {

    private String species;

    // Valeurs pour water, ground et azote
    private double valueWater;
    private double valueGround;
    private double valueAzote;

    // Fiabilités pour water, ground et azote
    private double reliabilityWater;
    private double reliabilityGround;
    private double reliabilityAzote;


    public PlantFullService() {
        // Constructeur vide
    }

    public PlantFullService(String species,
                            double valueWater, double valueGround, double valueAzote,
                            double reliabilityWater, double reliabilityGround, double reliabilityAzote) {

        this.species = species;
        this.valueWater = valueWater;
        this.valueGround = valueGround;
        this.valueAzote = valueAzote;
        this.reliabilityWater = reliabilityWater;
        this.reliabilityGround = reliabilityGround;
        this.reliabilityAzote = reliabilityAzote;
    }

    // Getters et setters

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public double getValueWater() {
        return valueWater;
    }

    public void setValueWater(double valueWater) {
        this.valueWater = valueWater;
    }

    public double getValueGround() {
        return valueGround;
    }

    public void setValueGround(double valueGround) {
        this.valueGround = valueGround;
    }

    public double getValueAzote() {
        return valueAzote;
    }

    public void setValueAzote(double valueAzote) {
        this.valueAzote = valueAzote;
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


    @Override
    public String toString() {
        return "PlantService{" +
                ", species='" + species + '\'' +
                ", valueWater=" + valueWater +
                ", valueGround=" + valueGround +
                ", valueAzote=" + valueAzote +
                ", reliabilityWater=" + reliabilityWater +
                ", reliabilityGround=" + reliabilityGround +
                ", reliabilityAzote=" + reliabilityAzote +
                '}';
    }
}
