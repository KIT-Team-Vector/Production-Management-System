package edu.kit.ordermanager.handlers;

import org.springframework.context.ConfigurableApplicationContext;

public interface IMessageHandler {

    public boolean sendDecreaseResourceSetRequest(String message);
}
