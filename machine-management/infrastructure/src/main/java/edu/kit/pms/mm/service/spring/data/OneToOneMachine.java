package edu.kit.pms.mm.service.spring.data;

import edu.kit.pms.mm.core.Machine;

import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;
import edu.kit.pms.mm.core.exceptions.ProductionException;
import edu.kit.pms.mm.service.spring.production.coreImpl.ResourceImpl;
import edu.kit.pms.mm.service.spring.production.coreImpl.ResourceSetImpl;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OneToOneMachine implements Machine {

    private static final int PRODUCTION_MULTIPLIER = 1;

    @Id
    private Integer id;
    private int inputResourceId;
    private int outputResourceId;

    public void setId(Integer newId) {
        if (newId <= 0) {
            throw new IllegalArgumentException("Machine ID must be greater than 0");
        }

        this.id = newId;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setInput(Resource newInputResource) {
        setInput(newInputResource.id());
    }

    public void setInput(int newInputResourceId) {
        this.inputResourceId = newInputResourceId;
    }

    @Override
    public Resource getInput() {
        return new ResourceImpl(inputResourceId);
    }

    public void setOutput(Resource newOutputResource) {
        setOutput(newOutputResource.id());
    }

    public void setOutput(int newOutputResourceId) {
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
