package edu.kit.pms.mm.app.test;

import edu.kit.pms.mm.app.ProductionManager;
import edu.kit.pms.mm.core.*;
import edu.kit.pms.mm.core.exceptions.InventoryException;
import edu.kit.pms.mm.core.exceptions.ProductionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class ProductionManagerTest {
    private static final int MOCKED_DESIRED_RESOURCE_ID = 404;
    private static final String MOCKED_DESIRED_RESOURCE_NAME = "MockedDesiredResource";

    private final MachineRepository<Machine> mockedMachineRepository = mock();
    private final Inventory mockedInventory = mock();
    private final Machine mockedMachine = mock();
    private final ResourceSet mockedDesiredResourceSet = mock();
    private final Resource mockedDesiredResource = mock();
    private final Resource mockedRequiredResource = mock();
    private ProductionManager productionManager;

    @BeforeEach
    public void setup() {
        productionManager = new ProductionManager(mockedMachineRepository, mockedInventory);
        when(mockedDesiredResourceSet.resource()).thenReturn(mockedDesiredResource);
        when(mockedDesiredResource.name()).thenReturn(MOCKED_DESIRED_RESOURCE_NAME);
        when(mockedDesiredResource.id()).thenReturn(MOCKED_DESIRED_RESOURCE_ID);
        when(mockedMachine.getInput()).thenReturn(mockedRequiredResource);
    }

    @Test
    public void test_produce_successful() throws InventoryException, ProductionException {
        when(mockedMachineRepository.find(any())).thenReturn(Collections.singletonList(mockedMachine));
        when(mockedInventory.get(any(), anyInt())).thenReturn(mockedDesiredResourceSet);
        when(mockedMachine.getMultiplier()).thenReturn(7);
        when(mockedDesiredResourceSet.amount()).thenReturn(10);

        boolean productionSuccessful = productionManager.produce(mockedDesiredResourceSet);

        assertThat(productionSuccessful).isTrue();
        verify(mockedInventory, times(1)).get(mockedRequiredResource, 2);
    }

    @Test
    public void test_produce_noMachineAvailable() {
        when(mockedMachineRepository.find(any())).thenReturn(Collections.emptyList());

        assertThatExceptionOfType(ProductionException.class).isThrownBy(() -> productionManager.produce(mockedDesiredResourceSet))
                .withMessageStartingWith("No machines available to produce resource ")
                .withNoCause();
    }

    @Test
    public void test_produce_insufficientResourcesAvailable() throws InventoryException, ProductionException {
        when(mockedMachineRepository.find(any())).thenReturn(Collections.singletonList(mockedMachine));
        when(mockedInventory.get(any(), anyInt())).thenReturn(null);
        when(mockedMachine.getMultiplier()).thenReturn(7);
        when(mockedDesiredResourceSet.amount()).thenReturn(7);

        boolean productionSuccessful = productionManager.produce(mockedDesiredResourceSet);

        assertThat(productionSuccessful).isFalse();
        verify(mockedInventory, times(1)).get(mockedRequiredResource, 1);
    }
}
