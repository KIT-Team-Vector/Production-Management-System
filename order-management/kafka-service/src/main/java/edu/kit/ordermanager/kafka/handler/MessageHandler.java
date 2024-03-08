package edu.kit.ordermanager.kafka.handler;

import edu.kit.ordermanager.handlers.IMessageHandler;
import edu.kit.ordermanager.kafka.consumer.MessageConsumer;
import edu.kit.ordermanager.kafka.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Component
public class MessageHandler implements IMessageHandler {

    @Autowired
    private MessageProducer messageProducer;

    public boolean sendDecreaseResourceSetRequest(String message) {
        messageProducer.sendMessage("decreaseResourceSetRequest", "Hello I am newwwwwwww");
        return true;
    }

}
