package edu.kit.ordermanager.controllers;

import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import edu.kit.ordermanager.handlers.IRestServiceHandler;
import edu.kit.pms.ordermanager.app.IRestService;


public class RestServiceAdapter implements IRestService {

    IRestServiceHandler restServiceHandler;
    private String checkInventoryUrl;
    private String checkMachineUrl;

    private String requiredResourceUrl;

    private String startProductionUrl;

    private void initUrls() {

        checkInventoryUrl = "http://" + System.getenv("INVENTORY_HOST")+ ":" + System.getenv("INVENTORY_PORT") + "/rest-service/inventory/resource/set";

        checkMachineUrl = "http://" + System.getenv("MACHINE_HOST")+ ":" + System.getenv("MACHINE_PORT") + "/pms/mm/checkMachine";

        requiredResourceUrl = "http://" + System.getenv("MACHINE_HOST") + ":" + System.getenv("MACHINE_PORT") + "/pms/mm/requiredResource";

        startProductionUrl = "http://" + System.getenv("MACHINE_HOST") + ":" + System.getenv("MACHINE_PORT") + "/pms/mm/produce";
    }

    /**
     * For tests
     * @param restServiceHandler
     */
    public RestServiceAdapter(IRestServiceHandler restServiceHandler) {
        this.restServiceHandler = restServiceHandler;
        initUrls();
    }

    @Override
    public ResourceSet checkInventory(Task order) {
        int resourceID = order.getResource().getId();
        String url = checkInventoryUrl + "/" + resourceID;

        return this.restServiceHandler.sendGetRequest(url, ResourceSet.class, null);
    }

    @Override
    public boolean checkAvailableMachinces(int resourceId) {
        String url = checkMachineUrl + "/" + resourceId;
        return Boolean.TRUE.equals(restServiceHandler.sendGetRequest(url, Boolean.class, null));
    }

    @Override
    public Resource findRequiredResource(int resourceId) {
        String url = requiredResourceUrl + "/" + resourceId;
        return restServiceHandler.sendGetRequest(url, Resource.class, null);
    }

    @Override
    public boolean startProduction(ResourceSet resourceSet) {
        return Boolean.TRUE.equals(restServiceHandler.sendPostRequest(startProductionUrl, resourceSet, Boolean.class));
    }

    public String getCheckInventoryUrl() {
        return this.checkInventoryUrl;
    }
}
