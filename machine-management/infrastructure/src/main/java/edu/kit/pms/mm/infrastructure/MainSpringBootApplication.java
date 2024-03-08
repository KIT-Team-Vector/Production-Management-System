package edu.kit.pms.mm.infrastructure;

import edu.kit.pms.mm.infrastructure.machines.MachineHandler;
import edu.kit.pms.mm.infrastructure.production.ProductionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainSpringBootApplication {

    public static void main(String[] args) {
        Class<?>[] handlers = new Class[2];
        handlers[0] = MachineHandler.class;
        handlers[1] = ProductionHandler.class;
        SpringApplication.run(handlers, args);
    }

}
