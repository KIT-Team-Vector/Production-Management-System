package edu.kit.ordermanager.controllers;

import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import edu.kit.pms.ordermanager.app.IRestServiceController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class RestServiceController implements IRestServiceController {

    RestTemplate restTemplate;

    String inventoryServiceHost = System.getenv("INVENTORY_HOST");

    String inventoryServicePort = System.getenv("INVENTORY_PORT");

    String machineServiceHost = System.getenv("MACHINE_HOST");

    String machineServicePort = System.getenv("MACHINE_PORT");
    private String checkInventoryUrl;
    private String checkMachineUrl;

    private String requiredResourceUrl;

    private String startProductionUrl;

    public RestServiceController() {
        initUrls();
    }

    private void initUrls() {
        inventoryServiceHost = System.getenv("INVENTORY_HOST");

        inventoryServicePort = System.getenv("INVENTORY_PORT");

        machineServiceHost = System.getenv("MACHINE_HOST");

        machineServicePort = System.getenv("MACHINE_PORT");

        checkInventoryUrl = "http://" + inventoryServiceHost+ ":" +inventoryServicePort + "/rest-service/inventory/resource/set";

        checkMachineUrl = "http://" + machineServiceHost+ ":" + machineServicePort + "/pms/mm/checkMachine";

        requiredResourceUrl = "http://" + machineServiceHost+ ":" + machineServicePort + "/pms/mm/requiredResource";

        startProductionUrl = "http://" + machineServiceHost+ ":" + machineServicePort + "/pms/mm/produce";
    }

    /**
     * For tests
     * @param restTemplate
     */
    public RestServiceController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        initUrls();
    }

    @Override
    public ResourceSet checkInventory(Task order) {
        int resourceID = order.getResource().getId();
        String url = checkInventoryUrl + "/" + resourceID;
        ResponseEntity<ResourceSet> responseEntity = restTemplate.getForEntity(url, ResourceSet.class);

        if(responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            return null;
        }

        return responseEntity.getBody();
    }

    @Override
    public boolean checkAvailableMachinces(int resourceId) {
        String url = checkMachineUrl + "/" + resourceId;
        return Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class));
    }

    @Override
    public Resource findRequiredResource(int resourceId) {
        String url = requiredResourceUrl + "/" + resourceId;
        return restTemplate.getForObject(url, Resource.class);
    }

    @Override
    public boolean startProduction(ResourceSet resourceSet) {
        return Boolean.TRUE.equals(restTemplate.postForObject(startProductionUrl, resourceSet, Boolean.class));
    }

    public String getCheckInventoryUrl() {
        return this.checkInventoryUrl;
    }
}
