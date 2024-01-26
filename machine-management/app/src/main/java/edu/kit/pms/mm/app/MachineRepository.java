package edu.kit.pms.mm.app;

import edu.kit.pms.mm.core.Machine;
import edu.kit.pms.mm.core.Resource;

import java.util.Collection;

public interface MachineRepository {

    boolean add(Machine machine);
    boolean remove(int id);
    boolean remove(Machine machine);
    Collection<Machine> find(Resource producedResource);

}