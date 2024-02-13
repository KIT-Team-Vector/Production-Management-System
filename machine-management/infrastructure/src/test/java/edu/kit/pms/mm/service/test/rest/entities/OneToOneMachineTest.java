package edu.kit.pms.mm.service.test.rest.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;
import edu.kit.pms.mm.core.exceptions.ProductionException;
import edu.kit.pms.mm.service.rest.coreImpl.OneToOneMachine;
import edu.kit.pms.mm.service.rest.coreImpl.ResourceImpl;
import edu.kit.pms.mm.service.rest.coreImpl.ResourceSetImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OneToOneMachineTest {
    private static final int MACHINE_ID = 1;
    private OneToOneMachine _simpleOneToOneMachine;
    private Resource _resourceOne;
    private Resource _resourceTwo;

    @BeforeEach
    public void setUp() {
        _resourceOne = new ResourceImpl(1);
        _resourceTwo = new ResourceImpl(2);

        _simpleOneToOneMachine = new OneToOneMachine();
        _simpleOneToOneMachine.setId(MACHINE_ID);
        _simpleOneToOneMachine.setInputResource(_resourceOne);
        _simpleOneToOneMachine.setOutputResource(_resourceTwo);
    }

    @Test
    public void test_creation_valid() {
        assertThat(_simpleOneToOneMachine.getId()).isEqualTo(MACHINE_ID);
        assertThat(_simpleOneToOneMachine.getInput()).isEqualTo(_resourceOne);
        assertThat(_simpleOneToOneMachine.getOutput()).isEqualTo(_resourceTwo);
    }

    @Test
    public void test_creation_invalidId() {
        assertThatThrownBy(() -> _simpleOneToOneMachine.setId(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Machine ID must be greater than 0");
    }

    @Test
    public void test_produce_valid() throws ProductionException {
        ResourceSet providedInputResourceSet = new ResourceSetImpl(_resourceOne, 10);
        ResourceSet expectedOutputResourceSet = new ResourceSetImpl(_resourceTwo, 10);

        ResourceSet actualProducedOutputSet = _simpleOneToOneMachine.produce(providedInputResourceSet);

        assertThat(actualProducedOutputSet).isEqualTo(expectedOutputResourceSet);
    }

    @Test
    public void test_produce_invalidProvidedResources() {
        ResourceSet providedInputResourceSet = new ResourceSetImpl(_resourceTwo, 10);

        assertThatThrownBy(() -> _simpleOneToOneMachine.produce(providedInputResourceSet))
                .isInstanceOf(ProductionException.class);
    }

}
