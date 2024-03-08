package edu.kit.ordermanager.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceSet {

    private Resource resource;

    private int amount;

    public ResourceSet (@JsonProperty("ressource") Resource resource, @JsonProperty("amount") int amount) {
        this.resource = resource;
        this.amount = amount;
    }

    public Resource getRessource() {
        return resource;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


}

