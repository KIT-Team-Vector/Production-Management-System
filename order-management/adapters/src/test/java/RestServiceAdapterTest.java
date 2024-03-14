import edu.kit.ordermanager.controllers.RestServiceAdapter;
import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import edu.kit.ordermanager.handlers.IRestServiceHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class RestServiceAdapterTest {

    @Mock
    IRestServiceHandler mockedRestServiceHandler;

    @InjectMocks
    private RestServiceAdapter restServiceAdapter;

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
        Task task = new Task(resource, amount);

        ResourceSet resourceSet = new ResourceSet(new Resource(id, name), amount);

        when(mockedRestServiceHandler.sendGetRequest(any(String.class), eq(ResourceSet.class), eq(null))).thenReturn(new ResourceSet(resource, amount));

        ResourceSet responseResourceSet = restServiceAdapter.checkInventory(task);
        assertEquals(resource.getId(), responseResourceSet.getResource().getId());
        assertEquals(amount, responseResourceSet.getAmount());
    }

    /*@Test
    public void testCheckInventory_noWithErrorCode() {
        int id = 1;
        String name = "steel";
        int amount = 3;

        Resource resource = new Resource(id, name);
        Task task = new Task(resource, amount);

        ResourceSet resourceSet = new ResourceSet(new Resource(id, name), amount);

        when(mockedRestServiceHandler.getForEntity(any(String.class), eq(ResourceSet.class))).thenReturn(new ResponseEntity<ResourceSet>(resourceSet, HttpStatus.NOT_FOUND));

        ResourceSet responseResourceSet = restServiceAdapter.checkInventory(task);
        assertNull(responseResourceSet);
    }*/
}
