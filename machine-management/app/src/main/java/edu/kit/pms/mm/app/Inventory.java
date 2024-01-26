package edu.kit.pms.mm.app;

import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;

public interface Inventory {

    boolean add(ResourceSet resourceSet);
    ResourceSet get(ResourceSet resourceSet);
    int checkQuantity(Resource resource);

}
