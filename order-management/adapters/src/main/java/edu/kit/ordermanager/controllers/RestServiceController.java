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
    private String checkInventoryUrl;
    private String checkMachineUrl;

    private String requiredResourceUrl;

    private String startProductionUrl;

    public RestServiceController() {
        this.restTemplate = new RestTemplate();
        initUrls();
    }

    private void initUrls() {

        checkInventoryUrl = "http://" + System.getenv("INVENTORY_HOST")+ ":" + System.getenv("INVENTORY_PORT") + "/rest-service/inventory/resource/set";

        checkMachineUrl = "http://" + System.getenv("MACHINE_HOST")+ ":" + System.getenv("MACHINE_PORT") + "/pms/mm/checkMachine";

        requiredResourceUrl = "http://" + System.getenv("MACHINE_HOST") + ":" + System.getenv("MACHINE_PORT") + "/pms/mm/requiredResource";

        startProductionUrl = "http://" + System.getenv("MACHINE_HOST") + ":" + System.getenv("MACHINE_PORT") + "/pms/mm/produce";
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

        System.out.println(responseEntity.getStatusCode().value());

        if(responseEntity.getStatusCode() != HttpStatus.OK) {
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
