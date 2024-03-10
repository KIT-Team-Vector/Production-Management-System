package edu.kit.ordermanager.controllers;

import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.handlers.IMessageHandler;
import edu.kit.pms.ordermanager.app.IKafkaController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Random;

@Controller
public class KafkaController implements IKafkaController {

    @Autowired
    private IMessageHandler messageHandler;
    @Override
    public boolean decreaseResourceSetRequest(ResourceSet resourceSet) {
        return messageHandler.sendDecreaseResourceSetRequest(new Random().nextLong(), resourceSet);
    }
}
