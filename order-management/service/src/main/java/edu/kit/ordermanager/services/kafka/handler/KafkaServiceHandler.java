package edu.kit.ordermanager.services.kafka.handler;

import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.handlers.IMessageHandler;
import edu.kit.ordermanager.services.kafka.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaServiceHandler implements IMessageHandler {

    @Value("${topic.decreaseResourceSetRequest}")
    private String topic;

    @Autowired
    private MessageProducer messageProducer;

    public boolean sendMessage(ResourceSet resourceSet) {
        return messageProducer.sendMessage(topic, resourceSet);
    }

}
