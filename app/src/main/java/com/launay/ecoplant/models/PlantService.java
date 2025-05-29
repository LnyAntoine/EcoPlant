package com.launay.ecoplant.models;

public class PlantService {

    private String service;
    private String species;
    private double value;
    private double reliability;
    private String culturalCondition;

    public PlantService() {
        // Constructeur vide pour frameworks ou sérialisation
    }

    public PlantService(String service, String species, double value, double reliability, String culturalCondition) {
        this.service = service;
        this.species = species;
        this.value = value;
        this.reliability = reliability;
        this.culturalCondition = culturalCondition;
    }

    // Getters et Setters

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getReliability() {
        return reliability;
    }

    public void setReliability(double reliability) {
        this.reliability = reliability;
    }

    public String getCulturalCondition() {
        return culturalCondition;
    }

    public void setCulturalCondition(String culturalCondition) {
        this.culturalCondition = culturalCondition;
    }

    @Override
    public String toString() {
        return "PlantService{" +
                "service='" + service + '\'' +
                ", species='" + species + '\'' +
                ", value=" + value +
                ", reliability=" + reliability +
                ", culturalCondition='" + culturalCondition + '\'' +
                '}';
    }
}

