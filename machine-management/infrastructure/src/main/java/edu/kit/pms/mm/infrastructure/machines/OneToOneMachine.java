package edu.kit.pms.mm.infrastructure.machines;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.kit.pms.mm.core.Machine;
import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;
import edu.kit.pms.mm.core.exceptions.ProductionException;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceImpl;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceSetImpl;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class OneToOneMachine implements Machine {

    private static final int PRODUCTION_MULTIPLIER = 1;

    @Id
    private Integer id;
    private int inputResourceId;
    private String inputResourceName = null;
    private int outputResourceId;
    private String outputResourceName = null;

    @Override
    public int getId() {
        return id;
    }

    public void setId(Integer newId) {
        if (newId <= 0) {
            throw new IllegalArgumentException("Machine ID must be greater than 0");
        }

        this.id = newId;
    }

    @Override
    public Resource getInput() {
        return new ResourceImpl(inputResourceId, inputResourceName);
    }

    public void setInput(Resource newInputResource) {
        setInput(newInputResource.id());
        inputResourceName = newInputResource.name();
    }

    public void setInput(int newInputResourceId) {
        this.inputResourceId = newInputResourceId;
    }

    @Override
    public Resource getOutput() {
        return new ResourceImpl(outputResourceId, outputResourceName);
    }

    public void setOutput(Resource newOutputResource) {
        setOutput(newOutputResource.id());
        outputResourceName = newOutputResource.name();
    }

    public void setOutput(int newOutputResourceId) {
        this.outputResourceId = newOutputResourceId;
    }

    @Override
    public int getMultiplier() {
        return PRODUCTION_MULTIPLIER;
    }

    @Override
    public ResourceSet produce(ResourceSet providedResourceSet) throws ProductionException {
        if (providedResourceSet.resource().id() == inputResourceId) {
            Resource outputResource = new ResourceImpl(outputResourceId, outputResourceName);
            int outputResourceAmount = providedResourceSet.amount() * PRODUCTION_MULTIPLIER;
            return new ResourceSetImpl(outputResource, outputResourceAmount);
        } else {
            throw new ProductionException("This machine does only work with resource " + inputResourceId + ", but resource " + providedResourceSet.resource().id() + " was provided");
        }
    }
}
