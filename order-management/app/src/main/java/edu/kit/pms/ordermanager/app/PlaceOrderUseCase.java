package edu.kit.pms.ordermanager.app;

import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;

public class PlaceOrderUseCase {

    private final IRestService restServiceController;

    private final IKafkaService kafkaController;

    public PlaceOrderUseCase(IRestService restServiceController, IKafkaService kafkaController) {
        this.restServiceController = restServiceController;
        this.kafkaController = kafkaController;
    }

    public boolean processOrder(Task order, boolean firstRun) {

        ResourceSet resourceSet= this.restServiceController.checkInventory(order);

        if(resourceSet == null) {
            return false;
        }

        if(resourceSet.getAmount() >= order.getAmount()) {
            if (firstRun) {
                //If amount of resources in inventory is enough take them out.
                resourceSet.setAmount(order.getAmount());
                kafkaController.decreaseResourceSetRequest(resourceSet);
            }
            return true;
        }

        if(resourceSet.getAmount() < order.getAmount()) {
            int difference = order.getAmount() - resourceSet.getAmount();
            //Consume API of maschine manager and ask for machines to produce the difference amount of the requested resource
            boolean machineAvailable = this.restServiceController.checkAvailableMachinces(order.getResource().getId());

            if(Boolean.TRUE.equals(machineAvailable)) {
                Resource resource = this.restServiceController.findRequiredResource(order.getResource().getId());
                Task childTask = new Task(resource, difference);

               if (this.processOrder(childTask, false)) {
                   if(this.restServiceController.startProduction(new ResourceSet(order.getResource(), difference)) && firstRun) {
                       return kafkaController.decreaseResourceSetRequest(new ResourceSet(order.getResource(), order.getAmount()));
                   }
               }
            }
        }
        return false;
    }
}
