package edu.kit.ordermanager.controller;

import edu.kit.ordermanager.controllers.RestServiceAdapter;
import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import edu.kit.pms.ordermanager.app.PlaceOrderUseCase;
import edu.kit.ordermanager.services.kafka.handler.KafkaServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderManagerController {
    private PlaceOrderUseCase placeOrder;

    @Autowired
    private KafkaServiceHandler kafkaServiceHandler;

    RestServiceAdapter restServiceAdapter;

    edu.kit.ordermanager.controllers.KafkaServiceAdapter kafkaController;

    public OrderManagerController(@Autowired RestServiceAdapter restServiceAdapter, @Autowired edu.kit.ordermanager.controllers.KafkaServiceAdapter kafkaController) {
        this.restServiceAdapter = restServiceAdapter;
        placeOrder = new PlaceOrderUseCase(restServiceAdapter, kafkaController);
    }

    @GetMapping("/index")
    public String index() {
        Resource resource = new Resource(2,"wood");
        ResourceSet resourceSet = new ResourceSet(resource, 1);
        kafkaServiceHandler.sendDecreaseResourceSetRequest(resourceSet);
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
