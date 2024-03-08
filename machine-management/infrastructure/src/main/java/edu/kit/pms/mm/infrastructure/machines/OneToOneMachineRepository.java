package edu.kit.pms.mm.infrastructure.machines;

import org.springframework.data.repository.CrudRepository;

public interface OneToOneMachineRepository extends CrudRepository<OneToOneMachine, Integer> {
}
