package edu.kit.pms.mm.app;

import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;

public interface AddInventory {

    boolean add(ResourceSet resourceSet);
    boolean add(Resource resource, int amount);
    boolean add(int resourceId, int amount);

}
