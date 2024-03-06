package edu.kit.pms.mm.infrastructure.machines;

import edu.kit.pms.mm.core.Machine;
import edu.kit.pms.mm.core.MachineRepository;
import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

@Controller
@RequestMapping(path = "/pms/mm")
@EnableAutoConfiguration
@ComponentScan
public class MachineHandler implements MachineRepository<OneToOneMachine> {

    @Autowired
    private OneToOneMachineRepository machineRepository;

    @PostMapping("/machines/add")
    public @ResponseBody boolean addMachine(@RequestParam int machineId, @RequestParam int inputResourceId, @RequestParam int outputResourceId) {
        OneToOneMachine machine = new OneToOneMachine();
        machine.setId(machineId);
        machine.setInput(inputResourceId);
        machine.setOutput(outputResourceId);

        return add(machine);
    }

    @DeleteMapping("/machines/remove/{machineId}")
    public @ResponseBody boolean removeMachine(@PathVariable int machineId) {
        return remove(machineId);
    }

    @GetMapping("/checkMachine/{resourceId}")
    public @ResponseBody boolean checkMachineAvailableToProduce(@PathVariable int resourceId) {
        return find(new ResourceImpl(resourceId)).stream().findFirst().isPresent();
    }

    @GetMapping("/requiredResource/{resourceId}")
    public @ResponseBody Resource getRequiredResource(@PathVariable int resourceId) {
        Machine machine = find(new ResourceImpl(resourceId)).stream().findFirst().orElse(null);

        if (machine == null) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "No machine available to produce resource " + resourceId);
        }

        return machine.getInput();
    }

    @Override
    public boolean add(OneToOneMachine machine) {
        if (machineRepository.existsById(machine.getId())) {
            return false;
        }
        machineRepository.save(machine);
        return true;
    }

    @Override
    public boolean remove(int id) {
        if (machineRepository.existsById(id)) {
            machineRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(OneToOneMachine machine) {
        return remove(machine.getId());
    }

    @Override
    public Collection<OneToOneMachine> find(Resource producedResource) {
        Collection<OneToOneMachine> foundMachines = new ArrayList<>();

        machineRepository.findAll().forEach(machine -> {
            if (machine.getOutput().equals(producedResource)) {
                foundMachines.add(machine);
            }
        });

        return foundMachines;
    }

    @Override
    public OneToOneMachine get(int id) {
        return machineRepository.findById(id).orElse(null);
    }

    @Override
    public Collection<OneToOneMachine> getAll() {
        Collection<OneToOneMachine> foundMachines = new ArrayList<>();
        machineRepository.findAll().forEach(foundMachines::add);
        return foundMachines;
    }
}
