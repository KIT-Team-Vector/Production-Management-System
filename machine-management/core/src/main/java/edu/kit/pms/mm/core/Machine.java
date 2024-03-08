package edu.kit.pms.mm.core;

import edu.kit.pms.mm.core.exceptions.ProductionException;

public interface Machine {

    int getId();

    Resource getInput();

    Resource getOutput();

    int getMultiplier();

    ResourceSet produce(ResourceSet providedResourceSet) throws ProductionException;

}