package edu.kit.pms.mm.app;

import edu.kit.pms.mm.core.*;
import edu.kit.pms.mm.core.exceptions.InventoryException;
import edu.kit.pms.mm.core.exceptions.ProductionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

public record ProductionManager(MachineRepository<? extends Machine> machineRepository, Inventory inventory) {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final AtomicLong PRODUCTION_REQUEST_COUNTER = new AtomicLong(0);

    public boolean produce(ResourceSet desiredResourceSet) throws ProductionException, InventoryException {
        LOGGER.info("Received production request #" + PRODUCTION_REQUEST_COUNTER.incrementAndGet() + ": " + desiredResourceSet);
        Collection<? extends Machine> availableMachines = machineRepository.find(desiredResourceSet.resource());

        if (availableMachines.isEmpty()) {
            LOGGER.error("Production request #" + PRODUCTION_REQUEST_COUNTER.get() + " failed because no machines are available");
            throw new ProductionException("No machines available to produce resource " + desiredResourceSet.resource());
        }

        Machine chosenMachine = availableMachines.iterator().next();
        Resource requiredResource = chosenMachine.getInput();

        ResourceSet availableResources = inventory.get(requiredResource, calcRequiredResourcesAmount(chosenMachine, desiredResourceSet));
        if (availableResources != null) {
            inventory.add(chosenMachine.produce(availableResources));
            LOGGER.info("Production request #" + PRODUCTION_REQUEST_COUNTER.get() + " finished successfully");
            return true;
        }

        LOGGER.warn("Production request #" + PRODUCTION_REQUEST_COUNTER.get() + " failed because no/insufficient resources are available");
        return false;
    }

    private int calcRequiredResourcesAmount(Machine machine, ResourceSet desiredResourceSet) {
        int machineMultiplier = machine.getMultiplier();
        int desiredResourceAmount = desiredResourceSet.amount();
        int requiredResourceAmount = desiredResourceAmount / machineMultiplier;

        return desiredResourceAmount % machineMultiplier == 0 ? requiredResourceAmount : requiredResourceAmount + 1;
    }
}
