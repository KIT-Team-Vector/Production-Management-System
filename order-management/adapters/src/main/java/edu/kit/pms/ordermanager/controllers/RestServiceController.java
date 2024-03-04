package edu.kit.pms.ordermanager.controllers;

import java.util.List;

import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import edu.kit.pms.ordermanager.app.IRestServiceController;
import edu.kit.pms.ordermanager.app.PlaceOrderUseCase;
import jakarta.persistence.criteria.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kit.ordermanager.rest.repository.TaskRepository;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/order")
public class RestServiceController implements IRestServiceController {

    private TaskRepository taskRepository;
    private PlaceOrderUseCase placeOrder;

    private final String inventoryApiUrl = "http://134.3.17.150:9092/rest-service/inventory/resource/set";
    private final String machineApiUrl = "http://<host>:<port>/pms/mm/checkMachine";

    private final String requiredResourceApiUrl = "http://<host>:<port>/pms/mm/requiredResource";

    private final String startProductionApiUrl = "http://<host>:<port>/pms/mm/produce";

    public RestServiceController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        placeOrder = new PlaceOrderUseCase(this);
    }

    @GetMapping("/index")
    public List<Task> index() {
        return this.taskRepository.findAll();
    }

    @GetMapping("/place")
    public void placeOrder(@RequestParam(value = "id") int id, @RequestParam(value = "name") String name, @RequestParam(value = "amount") int amount) {
        Resource resource = new Resource(id, name);
        Task order = new Task(resource, amount);
        placeOrder.processOrder(order);
        this.taskRepository.save(new Task(resource, amount));
    }


    @Override
    public ResponseEntity<ResourceSet> checkInventory(Task order) {
        int resourceID = order.getResource().getId();
        RestTemplate inventoryTemplate = new RestTemplate();
        String url = inventoryApiUrl + "/" + resourceID;
        return inventoryTemplate.exchange(url, HttpMethod.GET, null, ResourceSet.class);
    }

    @Override
    public boolean checkAvailableMachinces(int resourceId) {
        RestTemplate machineTemplate = new RestTemplate();
        String url = machineApiUrl + "/" + resourceId;
        return Boolean.TRUE.equals(machineTemplate.getForObject(url, Boolean.class));
    }

    @Override
    public Resource findRequiredResource(int resourceId) {
        RestTemplate requiredResourceTemplate = new RestTemplate();
        String url = requiredResourceApiUrl + "/" + resourceId;
        return requiredResourceTemplate.getForObject(url, Resource.class);
    }

    @Override
    public boolean startProduction(int resourceId) {
        RestTemplate startProductionTemplate = new RestTemplate();
        String url = startProductionApiUrl + "/" + resourceId;
        return Boolean.TRUE.equals(startProductionTemplate.getForObject(url, Boolean.class));
    }
}
