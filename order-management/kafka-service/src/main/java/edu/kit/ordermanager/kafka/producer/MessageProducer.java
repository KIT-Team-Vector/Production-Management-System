package edu.kit.ordermanager.kafka.producer;

import edu.kit.ordermanager.entities.ResourceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    @Autowired
    private KafkaTemplate<Long, ResourceSet> kafkaTemplate;

    public void sendMessage(String topic, Long key, ResourceSet resourceSet) {
        kafkaTemplate.send(topic, key, resourceSet);
    }

}
