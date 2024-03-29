package edu.kit.ordermanager.controller;

import edu.kit.ordermanager.controllers.InventoryServiceAdapter;
import edu.kit.ordermanager.controllers.MachineServiceAdapter;
import edu.kit.ordermanager.entities.Order;
import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.pms.ordermanager.app.PlaceOrderUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderManagerController {
    private final PlaceOrderUseCase placeOrder;

    MachineServiceAdapter machineServiceAdapter;

    public OrderManagerController(@Autowired MachineServiceAdapter machineServiceAdapter, @Autowired InventoryServiceAdapter kafkaController) {
        this.machineServiceAdapter = machineServiceAdapter;
        placeOrder = new PlaceOrderUseCase(machineServiceAdapter, kafkaController);
    }

    @GetMapping("/place")
    public ResourceSet placeOrder(@RequestParam(value = "id") int id, @RequestParam(value = "name") String name, @RequestParam(value = "amount") int amount) {
        Resource resource = new Resource(id, name);
        Order order = new Order(resource, amount);
        if(placeOrder.processOrder(order, true)) {
           return new ResourceSet(resource, amount);
        } else return new ResourceSet(resource, 0);
    }
}
