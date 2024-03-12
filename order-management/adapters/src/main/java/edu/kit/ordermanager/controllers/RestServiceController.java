package edu.kit.ordermanager.controllers;

import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import edu.kit.pms.ordermanager.app.IRestServiceController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class RestServiceController implements IRestServiceController {

    String inventoryServiceHost = System.getenv("INVENTORY_HOST");

    String inventoryServicePort = System.getenv("INVENTORY_PORT");

    String machineServiceHost = System.getenv("MACHINE_HOST");

    String machineServicePort = System.getenv("MACHINE_PORT");
    private final String checkInventoryUrl = "http://" + inventoryServiceHost+ ":" +inventoryServicePort + "/rest-service/inventory/resource/set";
    private final String checkMachineUrl = "http://" + machineServiceHost+ ":" + machineServicePort + "/pms/mm/checkMachine";

    private final String requiredResourceUrl = "http://" + machineServiceHost+ ":" + machineServicePort + "/pms/mm/requiredResource";

    private final String startProductionUrl = "http://" + machineServiceHost+ ":" + machineServicePort + "/pms/mm/produce";

    @Override
    public ResourceSet checkInventory(Task order) {
        int resourceID = order.getResource().getId();
        RestTemplate inventoryTemplate = new RestTemplate();
        String url = checkInventoryUrl + "/" + resourceID;
        ResponseEntity<ResourceSet> responseEntity = inventoryTemplate.getForEntity(url, ResourceSet.class);

        if(responseEntity.getStatusCode().value() == 404) {
            return null;
        }

        return responseEntity.getBody();
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
}
