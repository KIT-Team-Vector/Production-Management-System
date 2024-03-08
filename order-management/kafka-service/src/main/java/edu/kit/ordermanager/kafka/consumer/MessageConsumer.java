package edu.kit.ordermanager.kafka.consumer;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @KafkaListener(topics = "decreaseResourceSetResponse", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(@Header(KafkaHeaders.RECEIVED_KEY) Long key, Boolean response) {
        System.out.println("Received message customer response: " + response);
        if(key == 5000) {
            //TODO: Response to customer
        }

    }
}


