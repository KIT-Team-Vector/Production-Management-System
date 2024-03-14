package edu.kit.ordermanager.services.config;

import edu.kit.ordermanager.controllers.KafkaServiceAdapter;
import edu.kit.ordermanager.controllers.RestServiceAdapter;
import edu.kit.ordermanager.services.kafka.handler.KafkaServiceHandler;
import edu.kit.ordermanager.services.rest.RestServiceHandler;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Bean
    public RestServiceAdapter getRestServiceAdapter(RestServiceHandler restServiceHandler) {
        return new RestServiceAdapter(restServiceHandler);
    }

    @Bean
    public KafkaServiceAdapter getKafkaServiceAdapter(KafkaServiceHandler kafkaServiceHandler) {
        return new KafkaServiceAdapter(kafkaServiceHandler);
    }
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
