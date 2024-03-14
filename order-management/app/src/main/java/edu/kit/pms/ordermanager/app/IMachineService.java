package edu.kit.pms.ordermanager.app;

import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;

public interface IMachineService {

    public boolean checkAvailableMachines(int resourceId);

    public Resource findRequiredResource(int resourceId);

    public boolean startProduction(ResourceSet resourceSet);

}
