package edu.kit.pms.im.inventory;

import edu.kit.pms.im.domain.InventoryManagementError;
import edu.kit.pms.im.domain.ResourceSet;

public interface InventoryManager {

	/**
	 * Deletes the entire ResourceSet with id
	 * 
	 * @param id of the resourceSet
	 * @return success boolean value
	 */
	public boolean deleteResourceSet(int id);

	/**
	 * Gets the ResourceSet with id
	 * 
	 * @param id of the resourceSet
	 * @return resourceSet with given id
	 */
	public ResourceSet getResourceSet(int id);

	/**
	 * Adds the amount to an existing matching resource set (same id and name), or
	 * creates new ResourceSet
	 * 
	 * @param ResourceSet to be added
	 * @return added resourceSet
	 * @throws InventoryManagementError if resourceSet could not be added
	 */
	public ResourceSet addResourceSet(ResourceSet resourceSet) throws InventoryManagementError;

	/**
	 * Increases the amount of a ResourceSet, if you want to decease it use the
	 * decreaseResourceSet method
	 * 
	 * @param resourceSet, the amount of the ressourceSet is used as absolute
	 * @return success boolean value
	 * @throws InventoryManagementError, if resourceSet could't be found
	 */

	public boolean increaseResourceSet(ResourceSet resourceSet) throws InventoryManagementError;

	/**
	 * Decreases the amount of a resourceSet, if you want to increase it use the
	 * increaseResourceSet method
	 * 
	 * @param resourceSet, the amount of the ressourceSet is used as absolute
	 * @return success boolean value
	 * @throws InventoryManagementError, if resourceSet could't be found or amount drops
	 *                            below zero
	 */
	public boolean decreaseResourceSet(ResourceSet resourceSet) throws InventoryManagementError;
}
