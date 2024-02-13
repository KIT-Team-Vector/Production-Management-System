package edu.kit.pms.mm.core;

import java.util.Collection;

public interface MachineRepository<M extends Machine> {

    boolean add(M machine);
    boolean remove(int id);
    boolean remove(M machine);
    Collection<M> find(Resource producedResource);
    M get(int id);
    Collection<M> getAll();

}