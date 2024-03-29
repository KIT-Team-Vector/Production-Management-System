import edu.kit.ordermanager.controllers.InventoryServiceAdapter;
import edu.kit.ordermanager.entities.Order;
import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.handlers.IRestServiceHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class InventoryServiceAdapterTest {

    @Mock
    IRestServiceHandler mockedRestServiceHandler;

    @InjectMocks
    private InventoryServiceAdapter inventoryServiceAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testCheckInventory_noErrorCode() {
        int id = 1;
        String name = "steel";
        int amount = 3;

        Resource resource = new Resource(id, name);
        Order order = new Order(resource, amount);

        when(mockedRestServiceHandler.sendGetRequest(any(String.class), eq(ResourceSet.class), eq(null))).thenReturn(new ResourceSet(resource, amount));

        ResourceSet responseResourceSet = inventoryServiceAdapter.checkInventory(order);
        assertEquals(resource.getId(), responseResourceSet.getResource().getId());
        assertEquals(amount, responseResourceSet.getAmount());
    }
}
