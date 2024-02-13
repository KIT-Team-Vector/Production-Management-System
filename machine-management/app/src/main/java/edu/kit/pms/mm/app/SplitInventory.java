package edu.kit.pms.mm.app;

import edu.kit.pms.mm.core.Inventory;
import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;
import edu.kit.pms.mm.core.exceptions.InventoryException;

public class SplitInventory implements Inventory {

    private final AddInventory addInventory;
    private final GetInventory getInventory;

    public SplitInventory(AddInventory addInventory, GetInventory getInventory) {
        this.addInventory = addInventory;
        this.getInventory = getInventory;
    }

    @Override
    public boolean add(ResourceSet resourceSet) {
        return addInventory.add(resourceSet);
    }

    @Override
    public boolean add(Resource resource, int amount) {
        return addInventory.add(resource, amount);
    }

    @Override
    public boolean add(int resourceId, int amount) {
        return addInventory.add(resourceId, amount);
    }

    @Override
    public ResourceSet get(ResourceSet resourceSet) throws InventoryException {
        return getInventory.get(resourceSet);
    }

    @Override
    public ResourceSet get(Resource resource, int amount) throws InventoryException {
        return getInventory.get(resource, amount);
    }

    @Override
    public ResourceSet get(int resourceId, int amount) throws InventoryException {
        return getInventory.get(resourceId, amount);
    }
}
