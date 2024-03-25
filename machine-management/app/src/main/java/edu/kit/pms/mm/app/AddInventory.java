package edu.kit.pms.mm.app;

import edu.kit.pms.mm.core.Inventory;
import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;

/**
 * An AddInventory provides the methods to add Resources to an Inventory.
 *
 * @see Resource
 * @see ResourceSet
 * @see Inventory
 * @see GetInventory
 */
public interface AddInventory {

    /**
     * @see Inventory#add(ResourceSet)
     */
    boolean add(ResourceSet resourceSet);

    /**
     * @see Inventory#add(Resource, int)
     */
    boolean add(Resource resource, int amount);

    /**
     * @see Inventory#add(int, int)
     */
    boolean add(int resourceId, int amount);

}
