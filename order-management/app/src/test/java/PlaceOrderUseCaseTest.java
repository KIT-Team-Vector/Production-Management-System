import edu.kit.ordermanager.entities.Order;
import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.pms.ordermanager.app.IInventoryService;
import edu.kit.pms.ordermanager.app.IMachineService;
import edu.kit.pms.ordermanager.app.PlaceOrderUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlaceOrderUseCaseTest {


    private IMachineService machineService;

    private IInventoryService IInventoryService;

    private static PlaceOrderUseCase placeOrderUseCase;

    private Order order;

    private int id;

    private String name;

    private int amount;

    private Resource resource;
    @BeforeEach
    public void setUp() {
        machineService = mock(IMachineService.class);
        IInventoryService = mock(IInventoryService.class);
        placeOrderUseCase = new PlaceOrderUseCase(machineService, IInventoryService);
        MockitoAnnotations.openMocks(this);

        id = 1;
        name = "steel";
        amount = 3;

        resource = new Resource(id, name);
        order = new Order(resource, amount);
    }

    @Test
    public void test_EnoughResourcesInInventory_FirstRun() {
        when(IInventoryService.checkInventory(any(Order.class))).thenReturn(new ResourceSet(new Resource(id, name), amount));

        assertTrue(placeOrderUseCase.processOrder(order, true));
    }

    @Test
    public void test_EnoughResourcesInInventory_NotFirstRun() {
        when(IInventoryService.checkInventory(any(Order.class))).thenReturn(new ResourceSet(new Resource(id, name), amount));

        assertTrue(placeOrderUseCase.processOrder(order, false));
    }

    @Test
    public void test_EnoughResourcesInInventory_FirstRun_MachinesAvailable() {
        when(IInventoryService.checkInventory(any(Order.class))).thenReturn(new ResourceSet(new Resource(id, name), amount - 1));
        when(machineService.checkAvailableMachines(any(Integer.class))).thenReturn(true);
        when(machineService.findRequiredResource(any(Integer.class))).thenReturn(new Resource(2, "wood"));
        when(machineService.startProduction(any(ResourceSet.class))).thenReturn(true);
        when(IInventoryService.decreaseResourceSetRequest(any(ResourceSet.class))).thenReturn(true);

        assertTrue(placeOrderUseCase.processOrder(order, true));
    }

    @Test
    public void test_EnoughResourcesInInventory_NotFirstRun_MachinesAvailable() {
        when(IInventoryService.checkInventory(any(Order.class))).thenReturn(new ResourceSet(new Resource(id, name), amount - 1));
        when(machineService.checkAvailableMachines(any(Integer.class))).thenReturn(true);
        when(machineService.findRequiredResource(any(Integer.class))).thenReturn(new Resource(2, "wood"));
        when(machineService.startProduction(any(ResourceSet.class))).thenReturn(true);
        when(IInventoryService.decreaseResourceSetRequest(any(ResourceSet.class))).thenReturn(true);

        assertFalse(placeOrderUseCase.processOrder(order, false));
    }

    @Test
    public void test_EnoughResourcesInInventory_FirstRun_NoMachinesAvailable() {
        when(IInventoryService.checkInventory(any(Order.class))).thenReturn(new ResourceSet(new Resource(id, name), amount - 1));
        when(machineService.checkAvailableMachines(any(Integer.class))).thenReturn(false);
        when(machineService.findRequiredResource(any(Integer.class))).thenReturn(new Resource(2, "wood"));
        when(machineService.startProduction(any(ResourceSet.class))).thenReturn(true);
        when(IInventoryService.decreaseResourceSetRequest(any(ResourceSet.class))).thenReturn(true);

        assertFalse(placeOrderUseCase.processOrder(order, true));
    }

    @Test
    public void test_EnoughResourcesInInventory_NotFirstRun_NoMachinesAvailable() {
        when(IInventoryService.checkInventory(any(Order.class))).thenReturn(new ResourceSet(new Resource(id, name), amount - 1));
        when(machineService.checkAvailableMachines(any(Integer.class))).thenReturn(false);
        when(machineService.findRequiredResource(any(Integer.class))).thenReturn(new Resource(2, "wood"));
        when(machineService.startProduction(any(ResourceSet.class))).thenReturn(true);
        when(IInventoryService.decreaseResourceSetRequest(any(ResourceSet.class))).thenReturn(true);

        assertFalse(placeOrderUseCase.processOrder(order, false));
    }

    @AfterEach
    public void resetMocks() {
        Mockito.reset(machineService);
        Mockito.reset(IInventoryService);
        Mockito.clearAllCaches();
    }
}
