package edu.kit.ordermanager.handlers;

import org.springframework.context.ConfigurableApplicationContext;

public interface IMessageHandler {

    public void setContext(ConfigurableApplicationContext context);
}
