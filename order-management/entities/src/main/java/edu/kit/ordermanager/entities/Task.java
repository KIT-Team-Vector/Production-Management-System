package edu.kit.ordermanager.entities;

public class Task {

    private Long id;

    private Resource resource;

    //private Resource resource;

    private int amount;

    public Task() {

    }

    public Task(Resource resource, int amount) {
        this.resource = resource;
        this.amount = amount;
    }



    /*public Resource getResource() {
        return this.resource;
    }*/

    public int getAmount() {
        return this.amount;
    }

    public Resource getResource() {
        return this.resource;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
