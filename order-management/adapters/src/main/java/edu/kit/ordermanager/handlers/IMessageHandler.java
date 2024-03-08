package edu.kit.ordermanager.handlers;

import edu.kit.ordermanager.entities.ResourceSet;

public interface IMessageHandler {

    public boolean sendDecreaseResourceSetRequest(Long key, ResourceSet resourceSet);
}
