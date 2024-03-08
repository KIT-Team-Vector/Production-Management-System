package edu.kit.ordermanager.controllers;

import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import edu.kit.ordermanager.handlers.IMessageHandler;
import edu.kit.pms.ordermanager.app.IRestServiceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Controller
public class RestServiceController implements IRestServiceController {

    @Autowired
    private IMessageHandler messageHandler;
    String inventoryServiceHost = System.getenv("INVENTORY_HOST");

    String inventoryServicePort = System.getenv("INVENTORY_PORT");

    String machineServiceHost = System.getenv("MACHINE_HOST");

    String machineServicePort = System.getenv("MACHINE_PORT");
    private final String checkInventoryUrl = "http://" + inventoryServiceHost+ ":" +inventoryServicePort + "/rest-service/inventory/resource/set";
    private final String checkMachineUrl = "http://" + machineServiceHost+ ":" + machineServicePort + "/pms/mm/checkMachine";

    private final String requiredResourceUrl = "http://" + machineServiceHost+ ":" + machineServicePort + "/pms/mm/requiredResource";

    private final String startProductionUrl = "http://" + machineServiceHost+ ":" + machineServicePort + "/pms/mm/produce";

    @Override
    public ResponseEntity<ResourceSet> checkInventory(Task order) {
        int resourceID = order.getResource().getId();
        RestTemplate inventoryTemplate = new RestTemplate();
        String url = checkInventoryUrl + "/" + resourceID;
        return inventoryTemplate.exchange(url, HttpMethod.GET, null, ResourceSet.class);
    }

    @Override
    public boolean checkAvailableMachinces(int resourceId) {
        RestTemplate machineTemplate = new RestTemplate();
        String url = checkMachineUrl + "/" + resourceId;
        return Boolean.TRUE.equals(machineTemplate.getForObject(url, Boolean.class));
    }

    @Override
    public Resource findRequiredResource(int resourceId) {
        RestTemplate requiredResourceTemplate = new RestTemplate();
        String url = requiredResourceUrl + "/" + resourceId;
        return requiredResourceTemplate.getForObject(url, Resource.class);
    }

    @Override
    public boolean startProduction(ResourceSet resourceSet) {
        RestTemplate startProductionTemplate = new RestTemplate();
        return Boolean.TRUE.equals(startProductionTemplate.postForObject(startProductionUrl, resourceSet, Boolean.class));
    }

    public boolean decreaseResourceSetRequest(ResourceSet resourceSet) {
        return messageHandler.sendDecreaseResourceSetRequest(new Random().nextLong(), resourceSet);
    }
}
