package edu.kit.pms.mm.core;

/**
 * A Resource is a simplified representation of a resource in the real world.
 * A Resource is identified by its ID and for better understanding a name can also be provided.
 */
public interface Resource {

    /**
     * @return The ID of the Resource.
     */
    int id();

    /**
     * @return The name of the Resource.
     */
    String name();
}
