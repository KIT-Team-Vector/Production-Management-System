package edu.kit.ordermanager.services.kafka.handler;

import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.handlers.IKafkaServiceHandler;
import edu.kit.ordermanager.services.kafka.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaServiceHandler implements IKafkaServiceHandler {

    @Autowired
    private MessageProducer messageProducer;

    public boolean sendDecreaseResourceSetRequest(ResourceSet resourceSet) {
        return messageProducer.sendMessage("decreaseResourceSetRequest", resourceSet);
    }

}
