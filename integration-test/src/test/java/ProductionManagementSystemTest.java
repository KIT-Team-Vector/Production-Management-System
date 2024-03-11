import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pre-Condition: Docker must be fully build and deployed. This can be done with the docker compose.yaml in the Production-Management-System folder.
 */
public class ProductionManagementSystemTest {

    @Test
    public void placeOrderWithEnoughResourceInInventory() {

        int id = 1;
        String name = "steel";
        int amount = 2;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestOrder = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8080/order/place?id="+ id +"&name=" + name + "&amount=" + amount))
                .GET()
                .build();

        HttpRequest requestInventory = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8081/rest-service/inventory/resource/set/" + id))
                .GET()
                .build();

        this.sendRequests(client, requestInventory, requestOrder, id, amount, name);
    }

    public void sendRequests(HttpClient client,HttpRequest requestInventory, HttpRequest requestOrder, int id, int amount, String name) {

        try {
            HttpResponse<String> responseInventoryPreCondition = client.send(requestInventory, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> responseOrder = client.send(requestOrder, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> responseInventoryPostCondition = client.send(requestInventory, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = responseOrder.body();
            ResourceSet resourceSet = objectMapper.readValue(jsonString, ResourceSet.class);
            Resource resource = resourceSet.getResource();

            jsonString = responseInventoryPreCondition.body();
            ResourceSet resourceSetInventoryPre = objectMapper.readValue(jsonString, ResourceSet.class);
            Resource resourceInventoryPre = resourceSet.getResource();

            jsonString = responseInventoryPostCondition.body();
            ResourceSet resourceSetInventoryPost = objectMapper.readValue(jsonString, ResourceSet.class);
            Resource resourceInventoryPost = resourceSet.getResource();

            int currentAmount = resourceSetInventoryPre.getAmount() - amount;

            assertAll(() -> assertEquals(id, resource.getId()),
                    () -> assertEquals(name, resource.getName()),
                    () -> assertEquals(amount, resourceSet.getAmount()),
                    () -> assertEquals(currentAmount, resourceSetInventoryPost.getAmount()));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void placeOrderWithEnoughResourceInInventory2() {

        int id = 2;
        String name = "woood";
        int amount = 5;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestOrder = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8080/order/place?id="+ id +"&name=" + name + "&amount=" + amount))
                .GET()
                .build();

        HttpRequest requestInventory = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8081/rest-service/inventory/resource/set/" + id))
                .GET()
                .build();

        this.sendRequests(client, requestInventory, requestOrder, id, amount, name);
    }

    @Test
    public void placeOrderWithEnoughResourceInInventory3() {

        int id = 2;
        String name = "woood";
        int amount = 3;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestOrder = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8080/order/place?id="+ id +"&name=" + name + "&amount=" + amount))
                .GET()
                .build();

        HttpRequest requestInventory = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8081/rest-service/inventory/resource/set/" + id))
                .GET()
                .build();

        this.sendRequests(client, requestInventory, requestOrder, id, amount, name);
    }

    @Test
    public void placeOrderWithEnoughResourceInInventory4() {

        int id = 1;
        String name = "steel";
        int amount = 6;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestOrder = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8080/order/place?id="+ id +"&name=" + name + "&amount=" + amount))
                .GET()
                .build();

        HttpRequest requestInventory = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8081/rest-service/inventory/resource/set/" + id))
                .GET()
                .build();

        this.sendRequests(client, requestInventory, requestOrder, id, amount, name);
    }
}
