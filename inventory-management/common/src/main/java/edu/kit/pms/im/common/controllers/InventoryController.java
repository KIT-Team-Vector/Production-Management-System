package edu.kit.pms.im.common.controllers;

import edu.kit.pms.im.domain.ResourceSet;

public interface InventoryController {

	public ResourceSet getResourceSet(int id);

	public ResourceSet addResourceSet(ResourceSet resourceSet);

	public boolean removeResourceSet(int id);

	public void decreaseResourceSet(Long key, ResourceSet resourceSet);

}
