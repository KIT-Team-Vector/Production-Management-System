package edu.kit.pms;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceSet {

    private Resource resource;

    private int amount;

    public ResourceSet (@JsonProperty("resource") Resource resource, @JsonProperty("amount") int amount) {
        this.resource = resource;
        this.amount = amount;
    }

    public Resource getResource() {
        return resource;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


}

