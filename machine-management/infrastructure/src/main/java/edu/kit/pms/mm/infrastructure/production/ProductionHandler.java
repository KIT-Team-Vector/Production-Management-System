package edu.kit.pms.mm.infrastructure.production;

import edu.kit.pms.mm.app.AddInventory;
import edu.kit.pms.mm.app.GetInventory;
import edu.kit.pms.mm.app.ProductionManager;
import edu.kit.pms.mm.app.SplitInventory;
import edu.kit.pms.mm.core.ResourceSet;
import edu.kit.pms.mm.core.exceptions.InventoryException;
import edu.kit.pms.mm.core.exceptions.ProductionException;
import edu.kit.pms.mm.infrastructure.machines.MachineHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(path = "/pms/mm")
@EnableAutoConfiguration
@ComponentScan
public class ProductionHandler {

    @PostMapping("/produce")
    public @ResponseBody boolean produce(@RequestBody ResourceSet set, @Autowired ProductionManager productionManager) {
        try {
            return productionManager.produce(set);
        } catch (ProductionException | InventoryException e) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, e.getMessage());
        }
    }

    @Bean
    public ProductionManager getProductionManager(@Autowired MachineHandler machineRepo, @Autowired SplitInventory inventory) {
        return new ProductionManager(machineRepo, inventory);
    }

    @Bean
    public SplitInventory getSplitInventory(@Autowired AddInventory addInventory, @Autowired GetInventory getInventory) {
        return new SplitInventory(addInventory, getInventory);
    }

}
