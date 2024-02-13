package edu.kit.pms.mm.service.rest.coreImpl;

import edu.kit.pms.mm.core.Machine;

import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;
import edu.kit.pms.mm.core.exceptions.ProductionException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OneToOneMachine implements Machine {

    private static final int PRODUCTION_MULTIPLIER = 1;

    @Id
    private Integer id;
    private int inputResourceId;
    private int outputResourceId;

    public void setId(int newId) {
        if (newId <= 0) {
            throw new IllegalArgumentException("Machine ID must be greater than 0");
        }

        this.id = newId;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setInputResource(Resource newInputResource) {
        setInputResource(newInputResource.id());
    }

    public void setInputResource(int newInputResourceId) {
        this.inputResourceId = newInputResourceId;
    }

    @Override
    public Resource getInput() {
        return new ResourceImpl(inputResourceId);
    }

    public void setOutputResource(Resource newOutputResource) {
        setOutputResource(newOutputResource.id());
    }

    public void setOutputResource(int newOutputResourceId) {
        this.outputResourceId = newOutputResourceId;
    }

    @Override
    public Resource getOutput() {
        return new ResourceImpl(outputResourceId);
    }

    @Override
    public int getMultiplier() {
        return PRODUCTION_MULTIPLIER;
    }

    @Override
    public ResourceSet produce(ResourceSet providedResourceSet) throws ProductionException{
        if (providedResourceSet.resource().id() == inputResourceId) {
            Resource outputResource = new ResourceImpl(outputResourceId);
            int outputResourceAmount = providedResourceSet.amount() * PRODUCTION_MULTIPLIER;
            return new ResourceSetImpl(outputResource, outputResourceAmount);
        } else {
            throw new ProductionException("This machine does only work with resource " + inputResourceId + ", but resource " + providedResourceSet.resource().id() + " was provided");
        }
    }
}
