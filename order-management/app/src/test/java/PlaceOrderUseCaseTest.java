import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import edu.kit.pms.ordermanager.app.IKafkaService;
import edu.kit.pms.ordermanager.app.IRestService;
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


    private IRestService mockedRestService;

    private IKafkaService mockedKafka;

    private static PlaceOrderUseCase placeOrderUseCase;

    private Task task;

    private int id;

    private String name;

    private int amount;

    private Resource resource;
    @BeforeEach
    public void setUp() {
        mockedRestService = mock(IRestService.class);
        mockedKafka = mock(IKafkaService.class);
        placeOrderUseCase = new PlaceOrderUseCase(mockedRestService, mockedKafka);
        MockitoAnnotations.openMocks(this);

        id = 1;
        name = "steel";
        amount = 3;

        resource = new Resource(id, name);
        task = new Task(resource, amount);
    }

    @Test
    public void test_EnoughResourcesInInventory_FirstRun() {
        when(mockedRestService.checkInventory(any(Task.class))).thenReturn(new ResourceSet(new Resource(id, name), amount));

        assertTrue(placeOrderUseCase.processOrder(task, true));
    }

    @Test
    public void test_EnoughResourcesInInventory_NotFirstRun() {
        when(mockedRestService.checkInventory(any(Task.class))).thenReturn(new ResourceSet(new Resource(id, name), amount));

        assertTrue(placeOrderUseCase.processOrder(task, false));
    }

    @Test
    public void test_EnoughResourcesInInventory_FirstRun_MachinesAvailable() {
        when(mockedRestService.checkInventory(any(Task.class))).thenReturn(new ResourceSet(new Resource(id, name), amount - 1));
        when(mockedRestService.checkAvailableMachinces(any(Integer.class))).thenReturn(true);
        when(mockedRestService.findRequiredResource(any(Integer.class))).thenReturn(new Resource(2, "wood"));
        when(mockedRestService.startProduction(any(ResourceSet.class))).thenReturn(true);
        when(mockedKafka.decreaseResourceSetRequest(any(ResourceSet.class))).thenReturn(true);

        assertTrue(placeOrderUseCase.processOrder(task, true));
    }

    @Test
    public void test_EnoughResourcesInInventory_NotFirstRun_MachinesAvailable() {
        when(mockedRestService.checkInventory(any(Task.class))).thenReturn(new ResourceSet(new Resource(id, name), amount - 1));
        when(mockedRestService.checkAvailableMachinces(any(Integer.class))).thenReturn(true);
        when(mockedRestService.findRequiredResource(any(Integer.class))).thenReturn(new Resource(2, "wood"));
        when(mockedRestService.startProduction(any(ResourceSet.class))).thenReturn(true);
        when(mockedKafka.decreaseResourceSetRequest(any(ResourceSet.class))).thenReturn(true);

        assertFalse(placeOrderUseCase.processOrder(task, false));
    }

    @Test
    public void test_EnoughResourcesInInventory_FirstRun_NoMachinesAvailable() {
        when(mockedRestService.checkInventory(any(Task.class))).thenReturn(new ResourceSet(new Resource(id, name), amount - 1));
        when(mockedRestService.checkAvailableMachinces(any(Integer.class))).thenReturn(false);
        when(mockedRestService.findRequiredResource(any(Integer.class))).thenReturn(new Resource(2, "wood"));
        when(mockedRestService.startProduction(any(ResourceSet.class))).thenReturn(true);
        when(mockedKafka.decreaseResourceSetRequest(any(ResourceSet.class))).thenReturn(true);

        assertFalse(placeOrderUseCase.processOrder(task, true));
    }

    @Test
    public void test_EnoughResourcesInInventory_NotFirstRun_NoMachinesAvailable() {
        when(mockedRestService.checkInventory(any(Task.class))).thenReturn(new ResourceSet(new Resource(id, name), amount - 1));
        when(mockedRestService.checkAvailableMachinces(any(Integer.class))).thenReturn(false);
        when(mockedRestService.findRequiredResource(any(Integer.class))).thenReturn(new Resource(2, "wood"));
        when(mockedRestService.startProduction(any(ResourceSet.class))).thenReturn(true);
        when(mockedKafka.decreaseResourceSetRequest(any(ResourceSet.class))).thenReturn(true);

        assertFalse(placeOrderUseCase.processOrder(task, false));
    }

    @AfterEach
    public void resetMocks() {
        Mockito.reset(mockedRestService);
        Mockito.reset(mockedKafka);
        Mockito.clearAllCaches();
    }
}
