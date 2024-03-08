package edu.kit.pms.mm.infrastructure.test;

import edu.kit.pms.mm.core.exceptions.ProductionException;
import edu.kit.pms.mm.infrastructure.machines.OneToOneMachine;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceImpl;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceSetImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class OneToOneMachineTest {
    private static final int MACHINE_ID = 1;
    private OneToOneMachine _simpleOneToOneMachine;
    private ResourceImpl _resourceSteel;
    private ResourceImpl _resourceWood;

    @BeforeEach
    public void setUp() {
        _resourceSteel = new ResourceImpl(1, "steel");
        _resourceWood = new ResourceImpl(2, "wood");

        _simpleOneToOneMachine = new OneToOneMachine();
        _simpleOneToOneMachine.setId(MACHINE_ID);
        _simpleOneToOneMachine.setInput(_resourceSteel);
        _simpleOneToOneMachine.setOutput(_resourceWood);
    }

    @Test
    public void test_creation_valid() {
        assertThat(_simpleOneToOneMachine.getId()).isEqualTo(MACHINE_ID);
        assertThat(_simpleOneToOneMachine.getInput()).isEqualTo(_resourceSteel);
        assertThat(_simpleOneToOneMachine.getOutput()).isEqualTo(_resourceWood);
    }

    @Test
    public void test_creation_invalidId() {
        assertThatThrownBy(() -> _simpleOneToOneMachine.setId(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Machine ID must be greater than 0");
    }

    @Test
    public void test_produce_valid() throws ProductionException {
        edu.kit.pms.mm.core.ResourceSet providedInputResourceSet = new ResourceSetImpl(_resourceSteel, 10);
        edu.kit.pms.mm.core.ResourceSet expectedOutputResourceSet = new ResourceSetImpl(_resourceWood, 10);

        edu.kit.pms.mm.core.ResourceSet actualProducedOutputSet = _simpleOneToOneMachine.produce(providedInputResourceSet);

        assertThat(actualProducedOutputSet).isEqualTo(expectedOutputResourceSet);
    }

    @Test
    public void test_produce_invalidProvidedResources() {
        edu.kit.pms.mm.core.ResourceSet providedInputResourceSet = new ResourceSetImpl(_resourceWood, 10);

        assertThatThrownBy(() -> _simpleOneToOneMachine.produce(providedInputResourceSet))
                .isInstanceOf(ProductionException.class);
    }

}
