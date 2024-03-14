package edu.kit.pms.ordermanager.app;

import edu.kit.ordermanager.entities.Order;
import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;

public class PlaceOrderUseCase {

    private final IMachineService machineService;

    private final IInventoryService IInventoryService;

    public PlaceOrderUseCase(IMachineService machineService, IInventoryService IInventoryService) {
        this.machineService = machineService;
        this.IInventoryService = IInventoryService;
    }

    public boolean processOrder(Order order, boolean firstRun) {

        ResourceSet resourceSet= this.IInventoryService.checkInventory(order);

        if(resourceSet == null) {
            return false;
        }

        if(resourceSet.getAmount() >= order.getAmount()) {
            if (firstRun) {
                //If amount of resources in inventory is enough take them out.
                resourceSet.setAmount(order.getAmount());
                IInventoryService.decreaseResourceSetRequest(resourceSet);
            }
            return true;
        }

        if(resourceSet.getAmount() < order.getAmount()) {
            int difference = order.getAmount() - resourceSet.getAmount();
            //Consume API of maschine manager and ask for machines to produce the difference amount of the requested resource
            boolean machineAvailable = this.machineService.checkAvailableMachines(order.getResource().getId());

            if(Boolean.TRUE.equals(machineAvailable)) {
                Resource resource = this.machineService.findRequiredResource(order.getResource().getId());
                Order childOrder = new Order(resource, difference);

               if (this.processOrder(childOrder, false)) {
                   if(this.machineService.startProduction(new ResourceSet(order.getResource(), difference)) && firstRun) {
                       return IInventoryService.decreaseResourceSetRequest(new ResourceSet(order.getResource(), order.getAmount()));
                   }
               }
            }
        }
        return false;
    }
}
