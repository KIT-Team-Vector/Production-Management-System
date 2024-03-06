package edu.kit.pms.mm.app;

import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;
import edu.kit.pms.mm.core.exceptions.InventoryException;

public interface GetInventory {

    ResourceSet get(ResourceSet resourceSet) throws InventoryException;

    ResourceSet get(Resource resource, int amount) throws InventoryException;

    ResourceSet get(int resourceId, int amount) throws InventoryException;

}
