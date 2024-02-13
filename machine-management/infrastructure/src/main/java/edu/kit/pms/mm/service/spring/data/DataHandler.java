package edu.kit.pms.mm.service.spring.data;

import edu.kit.pms.mm.core.MachineRepository;
import edu.kit.pms.mm.core.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;

@Controller
@RequestMapping(path="/pms/mm/machines")
@EnableAutoConfiguration
@ComponentScan
public class DataHandler implements MachineRepository<OneToOneMachine> {

    @Autowired
    private OneToOneMachineRepository machineRepository;

    @PostMapping("/add")
    public @ResponseBody boolean addMachine(@RequestParam int machineId, @RequestParam int inputResourceId, @RequestParam int outputResourceId) {
        OneToOneMachine machine = new OneToOneMachine();
        machine.setId(machineId);
        machine.setInput(inputResourceId);
        machine.setOutput(outputResourceId);

        return add(machine);
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
