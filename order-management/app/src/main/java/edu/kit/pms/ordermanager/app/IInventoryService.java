package edu.kit.pms.ordermanager.app;

import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Order;

public interface IInventoryService {
    public boolean decreaseResourceSetRequest(ResourceSet resourceSet);

    public ResourceSet checkInventory(Order order);
    
}
