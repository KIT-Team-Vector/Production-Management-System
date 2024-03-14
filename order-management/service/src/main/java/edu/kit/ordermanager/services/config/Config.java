package edu.kit.ordermanager.services.config;

import edu.kit.ordermanager.controllers.InventoryServiceAdapter;
import edu.kit.ordermanager.controllers.MachineServiceAdapter;
import edu.kit.ordermanager.services.kafka.handler.KafkaServiceHandler;
import edu.kit.ordermanager.services.rest.RestServiceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Bean
    public MachineServiceAdapter getRestServiceAdapter(RestServiceHandler restServiceHandler) {
        return new MachineServiceAdapter(restServiceHandler);
    }

    @Bean
    public InventoryServiceAdapter getKafkaServiceAdapter(KafkaServiceHandler kafkaServiceHandler, RestServiceHandler restServiceHandler) {
        return new InventoryServiceAdapter(kafkaServiceHandler, restServiceHandler);
    }
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
