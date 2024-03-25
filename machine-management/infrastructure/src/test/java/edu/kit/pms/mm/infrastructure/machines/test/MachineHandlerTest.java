package edu.kit.pms.mm.infrastructure.machines.test;

import edu.kit.pms.mm.infrastructure.machines.MachineHandler;
import edu.kit.pms.mm.infrastructure.machines.OneToOneMachine;
import edu.kit.pms.mm.infrastructure.machines.OneToOneMachineRepository;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MachineHandlerTest {

    private static final OneToOneMachineRepository mockedMachineRepository = mock();
    private static final OneToOneMachine machine = new OneToOneMachine();
    private MachineHandler machineHandler;

    @BeforeAll
    public static void createMachines() {
        machine.setId(404);
        machine.setInput(new ResourceImpl(1, "steel"));
        machine.setOutput(new ResourceImpl(2, "wood"));
    }

    @BeforeEach
    public void setup() {
        machineHandler = new MachineHandler(mockedMachineRepository);
    }

    @Test
    public void test_add_successful() {
        when(mockedMachineRepository.existsById(anyInt())).thenReturn(false);

        boolean methodResult = machineHandler.add(machine);

        assertThat(methodResult).isTrue();
    }

    @Test
    public void test_add_machineAlreadyExists() {
        when(mockedMachineRepository.existsById(anyInt())).thenReturn(true);

        boolean methodResult = machineHandler.add(machine);

        assertThat(methodResult).isFalse();
    }

    @Test
    public void test_remove_successful() {
        when(mockedMachineRepository.existsById(anyInt())).thenReturn(true);

        boolean methodResult = machineHandler.remove(machine.getId());

        assertThat(methodResult).isTrue();
    }

    @Test
    public void test_remove_machineDoesNotExist() {
        when(mockedMachineRepository.existsById(anyInt())).thenReturn(false);

        boolean methodResult = machineHandler.remove(machine.getId());

        assertThat(methodResult).isFalse();
    }

    @Test
    public void test_find_foundMachine() {
        when(mockedMachineRepository.findAll()).thenReturn(List.of(machine));

        Collection<OneToOneMachine> foundMachines = machineHandler.find(new ResourceImpl(2, "wood"));

        assertThat(foundMachines).containsExactly(machine);
    }

    @Test
    public void test_find_noMachine() {
        when(mockedMachineRepository.findAll()).thenReturn(List.of(machine));

        Collection<OneToOneMachine> foundMachines = machineHandler.find(new ResourceImpl(1, "steel"));

        assertThat(foundMachines).isEmpty();
    }

    @Test
    public void test_get_successful() {
        when(mockedMachineRepository.findById(machine.getId())).thenReturn(Optional.of(machine));

        OneToOneMachine foundMachine = machineHandler.get(404);

        assertThat(foundMachine).isEqualTo(machine);
    }

    @Test
    public void test_get_noMachine() {
        when(mockedMachineRepository.findById(any())).thenReturn(Optional.empty());

        OneToOneMachine foundMachine = machineHandler.get(404);

        assertThat(foundMachine).isEqualTo(null);
    }

}
