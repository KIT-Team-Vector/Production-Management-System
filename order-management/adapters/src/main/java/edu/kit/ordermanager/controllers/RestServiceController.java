package edu.kit.ordermanager.controllers;

import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import edu.kit.ordermanager.handlers.IMessageHandler;
import edu.kit.pms.ordermanager.app.IRestServiceController;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Random;


public class RestServiceController implements IRestServiceController {

    private IMessageHandler messageHandler;
    private final String inventoryApiUrl = "http://134.3.17.150:9092/rest-service/inventory/resource/set";
    private final String machineApiUrl = "http://<host>:<port>/pms/mm/checkMachine";

    private final String requiredResourceApiUrl = "http://<host>:<port>/pms/mm/requiredResource";

    private final String startProductionApiUrl = "http://<host>:<port>/pms/mm/produce";

    public RestServiceController(IMessageHandler messageHandler) {

        this.messageHandler = messageHandler;
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

    public boolean decreaseResourceSetRequest(ResourceSet resourceSet) {
        return messageHandler.sendDecreaseResourceSetRequest(new Random().nextLong(), resourceSet);
    }
}
