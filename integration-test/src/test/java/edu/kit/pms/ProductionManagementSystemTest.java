package edu.kit.pms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pre-Condition: Docker must be fully build and deployed. This can be done with the docker compose.yaml in the Production-Management-System folder.
 * Expected inventory-db and machine-db status is specified in init.sql and initMachineManagementDB.sql file.
 */
public class ProductionManagementSystemTest {

    @BeforeAll
    public static void setUp() {

        for(int i = 1; i <= 5; i++) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest requestInventory = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .uri(URI.create("http://localhost:8081/rest-service/inventory/resource/set/" + i))
                    .GET()
                    .build();

            try {
                HttpResponse<String> responseInventory = client.send(requestInventory, HttpResponse.BodyHandlers.ofString());
                ResourceSet resourceSet = parser(responseInventory.body());
                if(resourceSet.getAmount() != 10) {
                    resourceSet.setAmount(10);
                    ObjectMapper ow = new ObjectMapper();
                    String jsnResourceSet = ow.writeValueAsString(resourceSet);
                    HttpRequest requestAddToInventory = HttpRequest.newBuilder()
                            .version(HttpClient.Version.HTTP_2)
                            .uri(URI.create("http://localhost:8081/rest-service/inventory/resource/set"))
                            .POST(HttpRequest.BodyPublishers.ofString(jsnResourceSet))
                            .header("Content-type", "application/json")
                            .build();
                    client.send(requestAddToInventory, HttpResponse.BodyHandlers.ofString());
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    @Order(1)
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

        this.sendRequestsWithoutProduction(client, requestInventory, requestOrder, id, amount, name);
    }

    @Test
    @Order(2)
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

        this.sendRequestsWithoutProduction(client, requestInventory, requestOrder, id, amount, name);
    }

    @Test
    @Order(3)
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

        this.sendRequestsWithoutProduction(client, requestInventory, requestOrder, id, amount, name);
    }

    @Test
    @Order(4)
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

        this.sendRequestsWithoutProduction(client, requestInventory, requestOrder, id, amount, name);
    }

    @Test
    @Order(5)
    public void placeOrderWithNotEnoughResourceInInventory() {

        int orderedResourceId = 4;
        String orderedResourceName = "paper";
        int orderedResourceAmount = 11;

        int neededForProductionResourceId = 3;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestOrder = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8080/order/place?id="+ orderedResourceId +"&name=" + orderedResourceName + "&amount=" + orderedResourceAmount))
                .GET()
                .build();

        HttpRequest requestInventory = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8081/rest-service/inventory/resource/set/" + orderedResourceId))
                .GET()
                .build();

        HttpRequest neededForProductionRequest = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8081/rest-service/inventory/resource/set/" + neededForProductionResourceId))
                .GET()
                .build();

        this.sendRequestsWithProduction(client, requestInventory, requestOrder, neededForProductionRequest, orderedResourceId, orderedResourceAmount, orderedResourceName);
    }

    @Test
    @Order(6)
    public void placeOrderWithNotEnoughResourceInInventory2() {

        int orderedResourceId = 1;
        String orderedResourceName = "steel";
        int orderedResourceAmount = 10;

        int neededForProductionResourceId = 5;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestOrder = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8080/order/place?id="+ orderedResourceId +"&name=" + orderedResourceName + "&amount=" + orderedResourceAmount))
                .GET()
                .build();

        HttpRequest requestInventory = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8081/rest-service/inventory/resource/set/" + orderedResourceId))
                .GET()
                .build();

        HttpRequest neededForProductionRequest = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://localhost:8081/rest-service/inventory/resource/set/" + neededForProductionResourceId))
                .GET()
                .build();

        this.sendRequestsWithProduction(client, requestInventory, requestOrder, neededForProductionRequest, orderedResourceId, orderedResourceAmount, orderedResourceName);
    }

    public void sendRequestsWithoutProduction(HttpClient client, HttpRequest requestInventory, HttpRequest requestOrder, int id, int amount, String name) {

        try {
            HttpResponse<String> responseInventoryPreCondition = client.send(requestInventory, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> responseOrder = client.send(requestOrder, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> responseInventoryPostCondition = client.send(requestInventory, HttpResponse.BodyHandlers.ofString());

            ResourceSet resourceSet = this.parser(responseOrder.body());
            Resource resource = resourceSet.getResource();


            ResourceSet resourceSetInventoryPre = parser(responseInventoryPreCondition.body());
            Resource resourceInventoryPre = resourceSet.getResource();

            ResourceSet resourceSetInventoryPost = parser(responseInventoryPostCondition.body());
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

    public void sendRequestsWithProduction(HttpClient client, HttpRequest requestInventoryWantedResource, HttpRequest requestOrder, HttpRequest requestInventorySpendResource, int id, int amount, String name) {

        try {
            HttpResponse<String> responseInventoryWantedPreCondition = client.send(requestInventoryWantedResource, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> responseInventorySpendPreCondition = client.send(requestInventorySpendResource, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> responseOrder = client.send(requestOrder, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> responseInventoryWantedPostCondition = client.send(requestInventoryWantedResource, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> responseInventorySpendPostCondition = client.send(requestInventorySpendResource, HttpResponse.BodyHandlers.ofString());

            ResourceSet orderResourceSet = this.parser(responseOrder.body());

            ResourceSet resourceNeededToProducePre = this.parser(responseInventorySpendPreCondition.body());

            ResourceSet resourceNeededToProducePost = this.parser(responseInventorySpendPostCondition.body());

            ResourceSet orderedResourceInInventoryPre = this.parser(responseInventoryWantedPreCondition.body());

            ResourceSet orderedResourceInInventoryPost = this.parser(responseInventoryWantedPostCondition.body());

            int currentAmount = 0;

            assertAll(() -> assertEquals(id, orderResourceSet.getResource().getId()),
                    () -> assertEquals(name, orderResourceSet.getResource().getName()),
                    () -> assertEquals(amount, orderResourceSet.getAmount()),
                    () -> assertEquals(currentAmount, orderedResourceInInventoryPost.getAmount()));

            int expectedProducedAmount = amount - orderedResourceInInventoryPre.getAmount();
            if(expectedProducedAmount < 0) {
                expectedProducedAmount = 0;
            }

            int expectedResourceNeededToProduceAmount = resourceNeededToProducePre.getAmount() - expectedProducedAmount;

            assertEquals(expectedResourceNeededToProduceAmount, resourceNeededToProducePost.getAmount());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static ResourceSet parser(String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(body, ResourceSet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
