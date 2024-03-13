import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import edu.kit.pms.ordermanager.app.IKafkaController;
import edu.kit.pms.ordermanager.app.IRestServiceController;
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


    private IRestServiceController mockedRestServiceController;

    private IKafkaController mockedKafkaController;

    private static PlaceOrderUseCase placeOrderUseCase;

    private Task task;

    private int id;

    private String name;

    private int amount;

    private Resource resource;
    @BeforeEach
    public void setUp() {
        mockedRestServiceController = mock(IRestServiceController.class);
        mockedKafkaController = mock(IKafkaController.class);
        placeOrderUseCase = new PlaceOrderUseCase(mockedRestServiceController, mockedKafkaController);
        MockitoAnnotations.openMocks(this);

        id = 1;
        name = "steel";
        amount = 3;

        resource = new Resource(id, name);
        task = new Task(resource, amount);
    }

    @Test
    public void test_EnoughResourcesInInventory_FirstRun() {
        when(mockedRestServiceController.checkInventory(any(Task.class))).thenReturn(new ResourceSet(new Resource(id, name), amount));

        assertTrue(placeOrderUseCase.processOrder(task, true));
    }

    @Test
    public void test_EnoughResourcesInInventory_NotFirstRun() {
        when(mockedRestServiceController.checkInventory(any(Task.class))).thenReturn(new ResourceSet(new Resource(id, name), amount));

        assertTrue(placeOrderUseCase.processOrder(task, false));
    }

    @Test
    public void test_EnoughResourcesInInventory_FirstRun_MachinesAvailable() {
        when(mockedRestServiceController.checkInventory(any(Task.class))).thenReturn(new ResourceSet(new Resource(id, name), amount - 1));
        when(mockedRestServiceController.checkAvailableMachinces(any(Integer.class))).thenReturn(true);
        when(mockedRestServiceController.findRequiredResource(any(Integer.class))).thenReturn(new Resource(2, "wood"));
        when(mockedRestServiceController.startProduction(any(ResourceSet.class))).thenReturn(true);
        when(mockedKafkaController.decreaseResourceSetRequest(any(ResourceSet.class))).thenReturn(true);

        assertTrue(placeOrderUseCase.processOrder(task, true));
    }

    @Test
    public void test_EnoughResourcesInInventory_NotFirstRun_MachinesAvailable() {
        when(mockedRestServiceController.checkInventory(any(Task.class))).thenReturn(new ResourceSet(new Resource(id, name), amount - 1));
        when(mockedRestServiceController.checkAvailableMachinces(any(Integer.class))).thenReturn(true);
        when(mockedRestServiceController.findRequiredResource(any(Integer.class))).thenReturn(new Resource(2, "wood"));
        when(mockedRestServiceController.startProduction(any(ResourceSet.class))).thenReturn(true);
        when(mockedKafkaController.decreaseResourceSetRequest(any(ResourceSet.class))).thenReturn(true);

        assertFalse(placeOrderUseCase.processOrder(task, false));
    }

    @Test
    public void test_EnoughResourcesInInventory_FirstRun_NoMachinesAvailable() {
        when(mockedRestServiceController.checkInventory(any(Task.class))).thenReturn(new ResourceSet(new Resource(id, name), amount - 1));
        when(mockedRestServiceController.checkAvailableMachinces(any(Integer.class))).thenReturn(false);
        when(mockedRestServiceController.findRequiredResource(any(Integer.class))).thenReturn(new Resource(2, "wood"));
        when(mockedRestServiceController.startProduction(any(ResourceSet.class))).thenReturn(true);
        when(mockedKafkaController.decreaseResourceSetRequest(any(ResourceSet.class))).thenReturn(true);

        assertFalse(placeOrderUseCase.processOrder(task, true));
    }

    @Test
    public void test_EnoughResourcesInInventory_NotFirstRun_NoMachinesAvailable() {
        when(mockedRestServiceController.checkInventory(any(Task.class))).thenReturn(new ResourceSet(new Resource(id, name), amount - 1));
        when(mockedRestServiceController.checkAvailableMachinces(any(Integer.class))).thenReturn(false);
        when(mockedRestServiceController.findRequiredResource(any(Integer.class))).thenReturn(new Resource(2, "wood"));
        when(mockedRestServiceController.startProduction(any(ResourceSet.class))).thenReturn(true);
        when(mockedKafkaController.decreaseResourceSetRequest(any(ResourceSet.class))).thenReturn(true);

        assertFalse(placeOrderUseCase.processOrder(task, false));
    }

    @AfterEach
    public void resetMocks() {
        Mockito.reset(mockedRestServiceController);
        Mockito.reset(mockedKafkaController);
        Mockito.clearAllCaches();
    }
}
