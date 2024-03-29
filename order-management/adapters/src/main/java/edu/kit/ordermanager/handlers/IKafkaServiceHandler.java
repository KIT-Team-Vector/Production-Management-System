package edu.kit.ordermanager.handlers;

import edu.kit.ordermanager.entities.ResourceSet;

public interface IKafkaServiceHandler {

    public boolean sendDecreaseResourceSetRequest(ResourceSet resourceSet);
}
