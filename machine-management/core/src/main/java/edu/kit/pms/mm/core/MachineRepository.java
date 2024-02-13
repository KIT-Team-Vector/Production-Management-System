package edu.kit.pms.mm.core;

import java.util.Collection;

public interface MachineRepository {

    boolean add(Machine machine);
    boolean addAll(Collection<Machine> machines);
    boolean remove(int id);
    boolean remove(Machine machine);
    boolean removeAll(int... ids);
    boolean removeAll(Collection<Machine> machines);
    Machine get(int id);
    Collection<Machine> getAll();
    Collection<Machine> find(Resource producedResource);

}