package edu.kit.pms.ordermanager.app;

import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.entities.Task;
import org.springframework.web.client.RestTemplate;

public class PlaceOrderUseCase {

    private final String inventoryApiUrl = "http://134.3.17.150:9092/rest-service/inventory/resource/set";
    private final String machineApiUrl = "http://<host>:<port>/pms/mm/checkMachine";

    private final String requiredResourceApiUrl = "http://<host>:<port>/pms/mm/requiredResource";

    private final String startProductionApiUrl = "http://<host>:<port>/pms/mm/produce";
    public PlaceOrderUseCase() {

    }

    public boolean processOrder(Task order) {
        int resourceID = order.getResource().getId();
        RestTemplate inventoryTemplate = new RestTemplate();
        String url = inventoryApiUrl + "/" + resourceID;
        ResourceSet resourceSet = inventoryTemplate.getForObject(url, ResourceSet.class);

        if(resourceSet == null) {
            return false;
        }

        if(resourceSet.getAmount() == order.getAmount()) {
            //TODO abbuchen vom Inventory durch consumen der Schnittstelle
            return true;
        }

        if(resourceSet.getAmount() < order.getAmount()) {
            int difference = order.getAmount() - resourceSet.getAmount();
            //TODO: Consume API of maschine manager and ask for machine to produce the difference amount of the requested resource
            RestTemplate machineTemplate = new RestTemplate();
            url = machineApiUrl + "/" + resourceID;
            Boolean machineAvailable = machineTemplate.getForObject(url, Boolean.class);

            if(Boolean.TRUE.equals(machineAvailable)) {
                RestTemplate requiredResourceTemplate = new RestTemplate();
                url = requiredResourceApiUrl + "/" + resourceID;
                int requiredResourceId = inventoryTemplate.getForObject(url, Integer.class);

                Resource resource = new Resource(resourceID, "Stahl");
                Task childTask = new Task(resource, difference);

               if (this.processOrder(childTask)) {
                   RestTemplate startProductionTemplate = new RestTemplate();
                   url = startProductionApiUrl + "/" + resourceID;
                   return Boolean.TRUE.equals(startProductionTemplate.getForObject(url, Boolean.class));
               }
            }
        }
        return false;
    }
}
