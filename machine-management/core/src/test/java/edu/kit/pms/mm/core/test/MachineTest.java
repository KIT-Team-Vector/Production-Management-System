package edu.kit.pms.mm.core.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import edu.kit.pms.mm.core.Machine;
import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class MachineTest {

    private static final int SIMPLE_MACHINE_ID = 1;
    private Machine _simpleMachine;
    private Resource _resourceOne;
    private Resource _resourceTwo;
    private ResourceSet _resourceSetOne;
    private ResourceSet _resourceSetTwo;

    @BeforeEach
    public void setUp() {
        var machineInput = new ArrayList<ResourceSet>();
        var machineOutput = new ArrayList<ResourceSet>();
        _resourceOne = new Resource(1);
        _resourceTwo = new Resource(2);
        _resourceSetOne = new ResourceSet(_resourceOne, 1);
        _resourceSetTwo = new ResourceSet(_resourceTwo, 2);

        machineInput.add(_resourceSetOne);
        machineOutput.add(_resourceSetTwo);
        _simpleMachine = new Machine(SIMPLE_MACHINE_ID, machineInput, machineOutput);
    }

    @Test
    public void test_creation_valid() {
        assertThat(_simpleMachine.getId()).isEqualTo(SIMPLE_MACHINE_ID);
        assertThat(_simpleMachine.getInput()).containsExactly(_resourceSetOne);
        assertThat(_simpleMachine.getRequiredResources()).containsExactly(_resourceOne);
        assertThat(_simpleMachine.getOutput()).containsExactly(_resourceSetTwo);
        assertThat(_simpleMachine.getProducedResources()).containsExactly(_resourceTwo);
    }

    @Test
    public void test_creation_invalidId() {
        assertThatThrownBy(() -> new Machine(0, new ArrayList<>(), new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id must be greater than 0");
    }

    @Test
    public void test_creation_invalidOutput() {
        assertThatThrownBy(() -> new Machine(1, new ArrayList<>(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("output must not be null");
    }

    @Test
    public void test_produce_valid() {
        var providedRes = new ArrayList<ResourceSet>();
        var expectedRes = new ArrayList<ResourceSet>();
        providedRes.add(_resourceSetOne);
        expectedRes.add(_resourceSetTwo);

        assertThat(_simpleMachine.produce(providedRes)).isEqualTo(expectedRes);
    }

    @Test
    public void test_produce_invalidProvidedResources() {
        var providedRes = new ArrayList<ResourceSet>();
        providedRes.add(_resourceSetTwo);

        assertThatThrownBy(() -> _simpleMachine.produce(providedRes))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("providedResources must match input");
    }

}
