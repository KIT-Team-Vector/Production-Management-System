package edu.kit.pms.mm.service.rest;

import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("/checkMachineAvailable")
    public boolean checkMachineAvailableToProduce(@RequestParam Resource resource) {
        //TODO check if a machine that can produce the given resource is available
        return false;
    }

    @GetMapping("/requiredResource")
    public Resource getRequiredResources(@RequestParam Resource resource) {
        //TODO check what resource the available machine needs to produce the given resource
        return null;
    }

    @PostMapping("/produce")
    public boolean produce(@RequestParam ResourceSet resourceSet) {
        //TODO produce the given resourceSet and confirm if production was successful
        return false;
    }
}
