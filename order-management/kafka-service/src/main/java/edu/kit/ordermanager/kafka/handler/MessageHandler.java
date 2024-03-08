package edu.kit.ordermanager.kafka.handler;

import edu.kit.ordermanager.handlers.IMessageHandler;
import edu.kit.ordermanager.kafka.consumer.MessageConsumer;
import edu.kit.ordermanager.kafka.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MessageHandler implements IMessageHandler {

    private ConfigurableApplicationContext context;

    @Autowired
    private MessageProducer messageProducer;
    //MessageConsumer consumer = context.getBean(MessageConsumer.class);
    /*
     * Sending a Hello World message to topic 'baeldung'.
     * Must be received by both listeners with group foo
     * and bar with containerFactory fooKafkaListenerContainerFactory
     * and barKafkaListenerContainerFactory respectively.
     * It will also be received by the listener with
     * headersKafkaListenerContainerFactory as container factory.
     */
    public boolean sendDecreaseResourceSetRequest(String message) {
        messageProducer.sendMessage("${spring.kafka.consumer.group-id}", "Hello I am newwwwwwww");
        return true;
    }

    @Override
    public void setContext(ConfigurableApplicationContext context) {
        this.context = context;
    }

}
