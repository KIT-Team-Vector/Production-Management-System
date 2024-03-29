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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class OneToOneMachine implements Machine {

    private static final Logger LOGGER = LogManager.getLogger();
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
        id = newId;
    }

    @Override
    public Resource getInput() {
        return new ResourceImpl(inputResourceId, inputResourceName);
    }

    public void setInput(Resource newInputResource) {
        inputResourceId = newInputResource.id();
        inputResourceName = newInputResource.name();
    }

    @Override
    public Resource getOutput() {
        return new ResourceImpl(outputResourceId, outputResourceName);
    }

    public void setOutput(Resource newOutputResource) {
        outputResourceId = newOutputResource.id();
        outputResourceName = newOutputResource.name();
    }

    @Override
    public int getMultiplier() {
        return PRODUCTION_MULTIPLIER;
    }

    @Override
    public ResourceSet produce(ResourceSet providedResourceSet) throws ProductionException {
        if (providedResourceSet.resource().id() == inputResourceId) {
            ResourceImpl outputResource = new ResourceImpl(outputResourceId, outputResourceName);
            int outputResourceAmount = providedResourceSet.amount() * getMultiplier();
            ResourceSet producedResourceSet = new ResourceSetImpl(outputResource, outputResourceAmount);

            LOGGER.info("Produced " + producedResourceSet);
            return producedResourceSet;
        } else {
            LOGGER.warn("Production failed because wrong resources were provided");
            throw new ProductionException("This machine does only work with " + new ResourceImpl(inputResourceId, inputResourceName) + ", but " + providedResourceSet.resource().id() + " was provided");
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " (id: " + id +
                ", input: " + getInput() +
                ", output: " + getOutput() +
                ", multiplier: " + getMultiplier() +
                ")";
    }
}
