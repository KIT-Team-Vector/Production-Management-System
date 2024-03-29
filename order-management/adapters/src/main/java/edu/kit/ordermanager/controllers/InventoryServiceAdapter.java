package edu.kit.ordermanager.controllers;

import edu.kit.ordermanager.entities.Order;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.handlers.IMessageHandler;
import edu.kit.ordermanager.handlers.IRestServiceHandler;
import edu.kit.pms.ordermanager.app.IInventoryService;

public class InventoryServiceAdapter implements IInventoryService {

    private final IMessageHandler kafkaServiceHandler;

    private final IRestServiceHandler restServiceHandler;

    private final String checkInventoryUrl;


    public InventoryServiceAdapter(IMessageHandler kafkaServiceHandler, IRestServiceHandler restServiceHandler) {
        this.kafkaServiceHandler = kafkaServiceHandler;
        this.restServiceHandler = restServiceHandler;
        checkInventoryUrl = "http://" + System.getenv("INVENTORY_HOST")+ ":" + System.getenv("INVENTORY_PORT") + "/rest-service/inventory/resource/set";
    }

    @Override
    public ResourceSet checkInventory(Order order) {
        int resourceID = order.getResource().getId();
        String url = checkInventoryUrl + "/" + resourceID;

        return this.restServiceHandler.sendGetRequest(url, ResourceSet.class, null);
    }

    @Override
    public boolean decreaseResourceSetRequest(ResourceSet resourceSet) {
        return kafkaServiceHandler.sendMessage(resourceSet);
    }
}
