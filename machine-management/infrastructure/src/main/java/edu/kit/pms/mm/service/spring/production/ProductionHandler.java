package edu.kit.pms.mm.service.spring.production;

import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/pms/mm")
@EnableAutoConfiguration
@ComponentScan
public class ProductionHandler {

    @GetMapping("/checkMachineAvailable")
    public @ResponseBody boolean checkMachineAvailableToProduce(@RequestParam Resource resource) {
        //TODO check if a machine that can produce the given resource is available
        return false;
    }

    @GetMapping("/requiredResource")
    public @ResponseBody Resource getRequiredResources(@RequestParam Resource resource) {
        //TODO check what resource the available machine needs to produce the given resource
        return null;
    }

    @PostMapping("/produce")
    public @ResponseBody boolean produce(@RequestParam ResourceSet resourceSet) {
        //TODO produce the given resourceSet and confirm if production was successful
        return false;
    }
}
