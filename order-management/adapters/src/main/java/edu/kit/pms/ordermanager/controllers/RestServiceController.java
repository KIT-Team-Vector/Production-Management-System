package edu.kit.pms.ordermanager.controllers;

import java.util.List;

import edu.kit.ordermanager.entities.Task;
import edu.kit.pms.ordermanager.app.PlaceOrderUseCase;
import jakarta.persistence.criteria.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kit.ordermanager.rest.repository.TaskRepository;


@RestController
@RequestMapping("/order")
public class RestServiceController {

    private TaskRepository taskRepository;
    private PlaceOrderUseCase placeOrder;

    public RestServiceController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        placeOrder = new PlaceOrderUseCase();
    }

    @GetMapping("/index")
    public List<Task> index() {
        return this.taskRepository.findAll();
    }

    @GetMapping("/place")
    public void placeOrder(@RequestParam(value = "resource") String resource, @RequestParam(value = "amount") int amount) {
        Task order = new Task(resource, amount);
        placeOrder.processOrder(order);
        this.taskRepository.save(new Task(resource, amount));
    }
}
