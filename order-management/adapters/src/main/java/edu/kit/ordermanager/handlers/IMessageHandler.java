package edu.kit.ordermanager.handlers;

import edu.kit.ordermanager.entities.ResourceSet;

public interface IMessageHandler {

    boolean sendMessage(ResourceSet resourceSet);
}
