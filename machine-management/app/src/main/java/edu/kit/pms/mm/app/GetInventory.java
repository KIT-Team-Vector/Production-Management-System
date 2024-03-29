package edu.kit.pms.mm.app;

import edu.kit.pms.mm.core.Inventory;
import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;
import edu.kit.pms.mm.core.exceptions.InventoryException;

/**
 * A GetInventory provides the methods to get Resources from an Inventory.
 *
 * @see Resource
 * @see ResourceSet
 * @see Inventory
 * @see AddInventory
 */
public interface GetInventory {

    /**
     * @see Inventory#get(ResourceSet)
     */
    ResourceSet get(ResourceSet resourceSet) throws InventoryException;

    /**
     * @see Inventory#get(Resource, int)
     */
    ResourceSet get(Resource resource, int amount) throws InventoryException;

    /**
     * @see Inventory#get(int, int)
     */
    ResourceSet get(int resourceId, int amount) throws InventoryException;

}
