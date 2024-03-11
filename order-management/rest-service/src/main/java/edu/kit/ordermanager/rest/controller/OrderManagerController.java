package edu.kit.ordermanager.rest.controller;

import edu.kit.ordermanager.controllers.KafkaController;
import edu.kit.ordermanager.controllers.RestServiceController;
import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import edu.kit.ordermanager.kafka.handler.MessageHandler;
import edu.kit.pms.ordermanager.app.PlaceOrderUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/order")
public class OrderManagerController {
    private PlaceOrderUseCase placeOrder;

    @Autowired
    private MessageHandler messageHandler;

    RestServiceController restServiceController;

    KafkaController kafkaController;

    public OrderManagerController(@Autowired RestServiceController restServiceController, @Autowired KafkaController kafkaController) {
        this.restServiceController = restServiceController;
        placeOrder = new PlaceOrderUseCase(restServiceController, kafkaController);
    }

    @GetMapping("/index")
    public String index() {
        Resource resource = new Resource(2,"wood");
        ResourceSet resourceSet = new ResourceSet(resource, 1);
        messageHandler.sendDecreaseResourceSetRequest(new Random().nextLong(), resourceSet);
        return "Request send";
    }

    @GetMapping("/place")
    public ResourceSet placeOrder(@RequestParam(value = "id") int id, @RequestParam(value = "name") String name, @RequestParam(value = "amount") int amount) {
        Resource resource = new Resource(id, name);
        Task order = new Task(resource, amount);
        if(placeOrder.processOrder(order, true)) {
           return new ResourceSet(resource, amount);
        } else return new ResourceSet(resource, 0);
    }
}
