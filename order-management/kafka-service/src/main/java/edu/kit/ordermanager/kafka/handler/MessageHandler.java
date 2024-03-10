package edu.kit.ordermanager.kafka.handler;

import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.handlers.IMessageHandler;
import edu.kit.ordermanager.kafka.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler implements IMessageHandler {

    @Autowired
    private MessageProducer messageProducer;

    public boolean sendDecreaseResourceSetRequest(Long key, ResourceSet resourceSet) {
        return messageProducer.sendMessage("decreaseResourceSetRequest",key, resourceSet);
    }

}
