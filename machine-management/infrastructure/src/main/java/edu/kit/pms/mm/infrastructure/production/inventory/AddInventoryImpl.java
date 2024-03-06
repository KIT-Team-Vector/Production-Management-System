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

    public static final String INVENTORY_SET_REQUEST_URL = "http://localhost:8080/rest-service/inventory/resource/set";
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
        ResourceSet addedResourceSet;

        try {
            addedResourceSet = restTemplate.postForObject(INVENTORY_SET_REQUEST_URL, resourceSet, edu.kit.pms.mm.core.ResourceSet.class);
        } catch (RestClientException e) {
            return false;
        }

        return addedResourceSet != null;
    }

    @Override
    public boolean add(Resource resource, int amount) {
        return add(new ResourceSetImpl(resource, amount));
    }

    @Override
    public boolean add(int resourceId, int amount) {
        return add(new ResourceImpl(resourceId), amount);
    }
}
