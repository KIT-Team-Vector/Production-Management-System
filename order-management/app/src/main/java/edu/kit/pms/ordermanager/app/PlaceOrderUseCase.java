package edu.kit.pms.ordermanager.app;

import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import org.springframework.http.ResponseEntity;

public class PlaceOrderUseCase {

    private IRestServiceController restServiceController;

    public PlaceOrderUseCase(IRestServiceController restServiceController) {
        this.restServiceController = restServiceController;
    }

    public boolean processOrder(Task order) {

        ResponseEntity<ResourceSet> resourceSetEntity = this.restServiceController.checkInventory(order);

        if(resourceSetEntity.getStatusCode().value() == 404) {
            return false;
        }

        ResourceSet resourceSet = resourceSetEntity.getBody();

        if(resourceSet.getAmount() >= order.getAmount()) {
            //If amount of resources in inventory is enough take them out.
            resourceSet.setAmount(order.getAmount());
            restServiceController.decreaseResourceSetRequest(resourceSet);
            return true;
        }

        if(resourceSet.getAmount() < order.getAmount()) {
            int difference = order.getAmount() - resourceSet.getAmount();
            //Consume API of maschine manager and ask for machines to produce the difference amount of the requested resource
            boolean machineAvailable = this.restServiceController.checkAvailableMachinces(order.getResource().getId());

            if(Boolean.TRUE.equals(machineAvailable)) {
                Resource resource = this.restServiceController.findRequiredResource(order.getResource().getId());
                Task childTask = new Task(resource, difference);

               if (this.processOrder(childTask)) {
                   return this.restServiceController.startProduction(order.getResource());
               }
            }
        }
        return false;
    }
}
