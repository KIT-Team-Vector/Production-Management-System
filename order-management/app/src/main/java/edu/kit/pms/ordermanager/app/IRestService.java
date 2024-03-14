package edu.kit.pms.ordermanager.app;

import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;

public interface IRestService {

    public ResourceSet checkInventory(Task order);

    public boolean checkAvailableMachinces(int resourceId);

    public Resource findRequiredResource(int resourceId);

    public boolean startProduction(ResourceSet resourceSet);

}
