package edu.kit.pms.mm.core;

import edu.kit.pms.mm.core.exceptions.InventoryException;

/**
 * An Inventory is used to store and manage Resources. The two primary functions are to provide the ability to add
 * Resources to the Inventory and to get previously added Resources from the Inventory. Resources are stored in the
 * form of ResourceSets.
 *
 * @see Resource
 * @see ResourceSet
 */
public interface Inventory {

    /**
     * Adds the given ResourceSet to the inventory. If the Resource of the provided ResourceSet already exists in the
     * Inventory the amount will be increased by the amount of the provided ResourceSet.
     *
     * @param resourceSet The ResourceSet that shall be added to the Inventory
     * @return true iff the ResourceSet was successfully added to the Inventory
     */
    boolean add(ResourceSet resourceSet);

    /**
     * @see #add(ResourceSet)
     */
    boolean add(Resource resource, int amount);

    /**
     * @see #add(ResourceSet)
     */
    boolean add(int resourceId, int amount);

    /**
     * Gets the specified ResourceSet from the Inventory.
     *
     * @param resourceSet The desired ResourceSet.
     * @return The desired ResourceSet iff possible.
     * @throws InventoryException Iff the desired ResourceSet is unavailable.
     */
    ResourceSet get(ResourceSet resourceSet) throws InventoryException;

    /**
     * @see #get(ResourceSet)
     */
    ResourceSet get(Resource resource, int amount) throws InventoryException;

    /**
     * @see #get(ResourceSet)
     */
    ResourceSet get(int resourceId, int amount) throws InventoryException;
}
