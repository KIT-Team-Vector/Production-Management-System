package edu.kit.pms.mm.infrastructure.machines.test;

import edu.kit.pms.mm.core.ResourceSet;
import edu.kit.pms.mm.core.exceptions.ProductionException;
import edu.kit.pms.mm.infrastructure.machines.OneToOneMachine;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceImpl;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceSetImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class OneToOneMachineTest {
    private static final int SIMPLE_MACHINE_ID = 1;
    private static final ResourceImpl RESOURCE_STEEL = new ResourceImpl(1, "steel");
    private static final ResourceImpl RESOURCE_WOOD = new ResourceImpl(2, "wood");
    private OneToOneMachine machine;

    @BeforeEach
    public void setup() {
        machine = new OneToOneMachine();
        machine.setId(SIMPLE_MACHINE_ID);
        machine.setInput(RESOURCE_STEEL);
        machine.setOutput(RESOURCE_WOOD);
    }

    @Test
    public void test_setId_validId() {
        assertThatNoException().isThrownBy(() -> machine.setId(1));
        assertThatNoException().isThrownBy(() -> machine.setId(2));
    }

    @Test
    public void test_setId_invalidId() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> machine.setId(-1))
                .withMessage("Machine ID must be greater than 0");
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> machine.setId(0))
                .withMessage("Machine ID must be greater than 0");
    }

    @Test
    public void test_produce_successful() throws ProductionException {
        ResourceSet providedInputResourceSet = new ResourceSetImpl(RESOURCE_STEEL, 10);
        ResourceSet expectedOutputResourceSet = new ResourceSetImpl(RESOURCE_WOOD, 10);

        ResourceSet actualProducedOutputSet = machine.produce(providedInputResourceSet);

        assertThat(actualProducedOutputSet).isEqualTo(expectedOutputResourceSet);
    }

    @Test
    public void test_produce_wrongProvidedResourceSet() {
        ResourceSet invalidProvidedInputResourceSet = new ResourceSetImpl(RESOURCE_WOOD, 10);

        assertThatExceptionOfType(ProductionException.class).isThrownBy(() -> machine.produce(invalidProvidedInputResourceSet))
                .withMessageStartingWith("This machine does only work with ");
    }

}
