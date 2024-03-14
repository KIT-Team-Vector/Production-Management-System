package edu.kit.ordermanager.controllers;

import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.handlers.IKafkaServiceHandler;
import edu.kit.pms.ordermanager.app.IKafkaService;

public class KafkaServiceAdapter implements IKafkaService {

    private IKafkaServiceHandler kafkaServiceHandler;

    public KafkaServiceAdapter(IKafkaServiceHandler kafkaServiceHandler) {
        this.kafkaServiceHandler = kafkaServiceHandler;
    }
    @Override
    public boolean decreaseResourceSetRequest(ResourceSet resourceSet) {
        return kafkaServiceHandler.sendDecreaseResourceSetRequest(resourceSet);
    }
}
