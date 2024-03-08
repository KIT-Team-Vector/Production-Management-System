package edu.kit.pms.mm.app;

import edu.kit.pms.mm.core.*;
import edu.kit.pms.mm.core.exceptions.InventoryException;
import edu.kit.pms.mm.core.exceptions.ProductionException;

import java.util.Collection;

public class ProductionManager {

    private final MachineRepository<? extends Machine> machineRepository;
    private final Inventory inventory;

    public ProductionManager(MachineRepository<? extends Machine> machineRepository, Inventory inventory) {
        this.machineRepository = machineRepository;
        this.inventory = inventory;
    }

    public boolean produce(ResourceSet desiredResources) throws ProductionException, InventoryException {
        Collection<? extends Machine> availableMachines = machineRepository.find(desiredResources.resource());

        if (availableMachines.isEmpty()) {
            throw new ProductionException("No machines available to produce resource " + desiredResources.resource().id());
        }

        Machine chosenMachine = availableMachines.iterator().next();
        Resource requiredResource = chosenMachine.getInput();
        int requiredResourceAmount = desiredResources.amount() / chosenMachine.getMultiplier();
        requiredResourceAmount = desiredResources.amount() % chosenMachine.getMultiplier() == 0 ? requiredResourceAmount : requiredResourceAmount + chosenMachine.getMultiplier();

        ResourceSet availableResources = inventory.get(requiredResource, requiredResourceAmount);
        if (availableResources != null) {
            inventory.add(chosenMachine.produce(availableResources));
        }

        return true;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public MachineRepository<? extends Machine> getMachineRepository() {
        return machineRepository;
    }
}
