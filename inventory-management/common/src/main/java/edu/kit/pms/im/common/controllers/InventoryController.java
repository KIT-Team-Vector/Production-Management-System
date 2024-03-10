package edu.kit.pms.im.common.controllers;

import edu.kit.pms.im.domain.ResourceSet;

/**
 * Inventory Controller. Is designed to be a controller between database,
 * rest-service, message-service and forwards request to the InventoryManager
 */
public interface InventoryController {
	
	/**
	 * Get a specific resource set from the inventory
	 * 
	 * @param id of the resourceSet
	 * @return resourceSet or null if ResourceSet does not exist
	 */
	public ResourceSet getResourceSet(int id);

	
	/**
	 * Adds a resourceSet to the inventory
	 * 
	 * @param resourceSet to be added
	 * @return added resourceSet, can result in a new id
	 */
	public ResourceSet addResourceSet(ResourceSet resourceSet);
	
	
	/**
	 * Removes a resourceSet from the inventory
	 * 
	 * @param id of the resourceSet to be removed
	 * @return true if successful, else false
	 */
	public boolean removeResourceSet(int id);

	/**
	 * Decreases the amount of an existing resourceSet
	 * 
	 * @param key associated with the request, used to send the response
	 * @param resourceSet with the updated amount
	 */
	public void decreaseResourceSet(Long key, ResourceSet resourceSet);

}
