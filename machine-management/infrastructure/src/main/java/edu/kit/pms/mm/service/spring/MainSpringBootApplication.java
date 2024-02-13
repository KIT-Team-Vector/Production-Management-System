package edu.kit.pms.mm.service.spring;

import edu.kit.pms.mm.service.spring.data.DataHandler;
import edu.kit.pms.mm.service.spring.production.ProductionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainSpringBootApplication {

    public static void main(String[] args) {
        Class<?>[] primarySources = new Class[2];
        primarySources[0] = DataHandler.class;
        primarySources[1] = ProductionHandler.class;
        SpringApplication.run(primarySources, args);
    }

}
