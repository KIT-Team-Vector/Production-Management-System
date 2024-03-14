package edu.kit.ordermanager.services.kafka.producer;

import edu.kit.ordermanager.entities.ResourceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MessageProducer {

    private Long key;

    @Autowired
    private KafkaTemplate<Long, ResourceSet> kafkaTemplate;

    public boolean sendMessage(String topic, ResourceSet resourceSet) {
        synchronized (this) {
            this.key = key = new Random().nextLong();
            kafkaTemplate.send(topic, key, resourceSet);
            try {
                wait();
                return true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void response(Long key) {
        synchronized (this) {
            if(this.key != null && this.key.equals(key)) {
                notify();
            }
        }
    }

}
