package edu.kit.ordermanager.kafka.producer;

import edu.kit.ordermanager.entities.ResourceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    @Autowired
    private KafkaTemplate<Long, ResourceSet> kafkaTemplate;

    public synchronized boolean sendMessage(String topic, Long key, ResourceSet resourceSet) {
        synchronized (this) {
            kafkaTemplate.send(topic, key, resourceSet);
            try {
                wait();
                return true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void response() {
        synchronized (this) {
            notify();
        }
    }

}
