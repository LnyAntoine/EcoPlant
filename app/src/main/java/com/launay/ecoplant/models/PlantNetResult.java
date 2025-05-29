package com.launay.ecoplant.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantNetResult {
    private double score;
    private Species species;
    private Gbif gbif;
    private Powo powo;

    // Getters et setters
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public Species getSpecies() { return species; }
    public void setSpecies(Species species) { this.species = species; }

    public Gbif getGbif() { return gbif; }
    public void setGbif(Gbif gbif) { this.gbif = gbif; }

    public Powo getPowo() { return powo; }
    public void setPowo(Powo powo) { this.powo = powo; }

    // Sous-classes
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Species {
        private String scientificName;
        private String scientificNameWithoutAuthor;
        private String scientificNameAuthorship;
        private Genus genus;
        private Family family;
        private List<String> commonNames;

        public String getScientificName() { return scientificName; }
        public void setScientificName(String scientificName) { this.scientificName = scientificName; }

        public String getScientificNameWithoutAuthor() { return scientificNameWithoutAuthor; }
        public void setScientificNameWithoutAuthor(String scientificNameWithoutAuthor) { this.scientificNameWithoutAuthor = scientificNameWithoutAuthor; }

        public String getScientificNameAuthorship() { return scientificNameAuthorship; }
        public void setScientificNameAuthorship(String scientificNameAuthorship) { this.scientificNameAuthorship = scientificNameAuthorship; }

        public Genus getGenus() { return genus; }
        public void setGenus(Genus genus) { this.genus = genus; }

        public Family getFamily() { return family; }
        public void setFamily(Family family) { this.family = family; }

        public List<String> getCommonNames() { return commonNames; }
        public void setCommonNames(List<String> commonNames) { this.commonNames = commonNames; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Genus {
        private String scientificName;

        public String getScientificName() { return scientificName; }
        public void setScientificName(String scientificName) { this.scientificName = scientificName; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Family {
        private String scientificName;

        public String getScientificName() { return scientificName; }
        public void setScientificName(String scientificName) { this.scientificName = scientificName; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Gbif {
        private String id;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getUrl() {
            return id != null ? "https://www.gbif.org/species/" + id : null;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Powo {
        private String id;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getUrl() {
            return id != null ? "https://powo.science.kew.org/taxon/" + id : null;
        }
    }
}
