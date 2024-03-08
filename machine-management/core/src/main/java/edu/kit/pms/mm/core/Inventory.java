package edu.kit.pms.mm.core;

import edu.kit.pms.mm.core.exceptions.InventoryException;

public interface Inventory {

    boolean add(ResourceSet resourceSet);

    boolean add(Resource resource, int amount);

    boolean add(int resourceId, int amount);

    ResourceSet get(ResourceSet resourceSet) throws InventoryException;

    ResourceSet get(Resource resource, int amount) throws InventoryException;

    ResourceSet get(int resourceId, int amount) throws InventoryException;
}
