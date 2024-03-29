package edu.kit.pms.mm.core;

/**
 * A ResourceSet represents a set of a specific Resource.
 *
 * @see Resource
 */
public interface ResourceSet {

    /**
     * @return The Resource of the ResourceSet.
     */
    Resource resource();

    /**
     * @return The amount of Resources in the ResourceSet.
     */
    int amount();

}
