package edu.kit.pms.mm.core;

import java.util.Collection;

/**
 * A MachineRepository is used to hold and manage Machines.
 *
 * @param <M> The specific class of Machines this MachineRepository is managing.
 * @see Machine
 */
public interface MachineRepository<M extends Machine> {

    /**
     * Adds the provided Machine to the MachineRepository.
     *
     * @param machine The Machine that shall be added.
     * @return true iff the operation was successful.
     */
    boolean add(M machine);

    /**
     * Removes the Machine identified by the provided ID from the MachineRepository.
     *
     * @param id The ID of the Machine that shall be removed.
     * @return true iff the operation was successful.
     */
    boolean remove(int id);

    /**
     * Removes the provided Machine from the MachineRepository.
     *
     * @param machine The Machine that shall be removed.
     * @return true iff the operation was successful.
     */
    boolean remove(M machine);

    /**
     * Searches all Machines in the MachineRepository that produce the desired Resource.
     *
     * @param producedResource The desired output Resource.
     * @return A Collection containing all Machines that produce the desired Resource.
     */
    Collection<M> find(Resource producedResource);

    /**
     * Gets the Machine identified by the provided ID.
     *
     * @param id The ID of the Machine.
     * @return The Machine identified by the provided ID.
     */
    M get(int id);

    /**
     * @return All Machines in the MachineRepository.
     */
    Collection<M> getAll();

}