package edu.kit.pms.ordermanager.app;

import edu.kit.ordermanager.entities.ResourceSet;

public interface IKafkaController {
    public boolean decreaseResourceSetRequest(ResourceSet resourceSet);
    
}
