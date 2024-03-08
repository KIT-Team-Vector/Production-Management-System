package edu.kit.ordermanager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

public class Resource implements Serializable {

    private int id;
    private String name;

    public Resource (@JsonProperty("id") int id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    @JsonIgnore
    public Resource () {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

