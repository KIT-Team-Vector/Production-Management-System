package edu.kit.pms.mm.app;

import edu.kit.pms.mm.core.*;
import edu.kit.pms.mm.core.exceptions.ProductionException;

import java.util.Collection;

public class ProductionManager {

    private final MachineRepository machineRepository;
    private final Inventory inventory;

    public ProductionManager(MachineRepository machineRepository, Inventory inventory) {
        this.machineRepository = machineRepository;
        this.inventory = inventory;
    }

    public boolean produce(ResourceSet desiredResources) throws ProductionException {
        Collection<Machine> availableMachines = machineRepository.find(desiredResources.resource());

        if (availableMachines.isEmpty()) {
            throw new ProductionException("No machines available to produce resource " + desiredResources.resource().id());
        }

        Machine chosenMachine = availableMachines.iterator().next();
        Resource requiredResource = chosenMachine.getInput();
        int requiredResourceAmount = desiredResources.amount() / chosenMachine.getMultiplier();
        requiredResourceAmount = desiredResources.amount() % 2 == 0 ? requiredResourceAmount : requiredResourceAmount + 1;

        //TODO get required resources from inventory and add "produced" resources

        return true;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public MachineRepository getMachineRepository() {
        return machineRepository;
    }
}
