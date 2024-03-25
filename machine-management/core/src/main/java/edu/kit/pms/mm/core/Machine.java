package edu.kit.pms.mm.core;

import edu.kit.pms.mm.core.exceptions.ProductionException;

/**
 * A machine is used to produce an output Resource when provided with an input Resource.
 * During production a multiplier is applied. So given a certain amount of provided input Resources, the amount of the
 * output Resources will be the same amount but multiplied by the Machine's multiplier.
 *
 * @see Resource
 */
public interface Machine {

    /**
     * @return The machine's unique ID.
     */
    int getId();

    /**
     * @return The input Resource of this machine.
     */
    Resource getInput();

    /**
     * @return The output Resource of this machine.
     */
    Resource getOutput();

    /**
     * @return The production multiplier of this machine.
     */
    int getMultiplier();

    /**
     * Starts the production of this machine.
     *
     * @param providedResourceSet The provided Resources.
     * @return The produced Resources.
     * @throws ProductionException Iff an exception occurred during production.
     */
    ResourceSet produce(ResourceSet providedResourceSet) throws ProductionException;

}