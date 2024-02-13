package edu.kit.pms.im.inventory;

import edu.kit.pms.im.domain.ResourceSet;

public interface InventoryManager {
	
	public ResourceSet getResourceSet(int id);
	
	public void addResourceSet(ResourceSet resourceSet);
	
	public ResourceSet removeResourceSet(ResourceSet resourceSet);
	

}
