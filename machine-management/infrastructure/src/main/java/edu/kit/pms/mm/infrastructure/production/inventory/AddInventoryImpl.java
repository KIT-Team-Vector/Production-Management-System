package edu.kit.pms.mm.infrastructure.production.inventory;

import edu.kit.pms.mm.app.AddInventory;
import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceImpl;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceSetImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AddInventoryImpl implements AddInventory {
    private final RestTemplate restTemplate;

    @Autowired
    public AddInventoryImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public AddInventoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean add(ResourceSet resourceSet) {
        ResourceSetImpl addedResourceSet;
        String inventoryServiceHost = System.getenv("INVENTORY_HOST");
        String inventoryServicePort = System.getenv("INVENTORY_PORT");

        try {
            addedResourceSet = restTemplate.postForObject("http://" + inventoryServiceHost + ":" + inventoryServicePort + "/rest-service/inventory/resource/set", resourceSet, ResourceSetImpl.class);
        } catch (RestClientException e) {
            return false;
        }

        return addedResourceSet != null;
    }

    @Override
    public boolean add(Resource resource, int amount) {
        return add(new ResourceSetImpl((ResourceImpl) resource, amount));
    }

    @Override
    public boolean add(int resourceId, int amount) {
        return add(new ResourceImpl(resourceId), amount);
    }
}
