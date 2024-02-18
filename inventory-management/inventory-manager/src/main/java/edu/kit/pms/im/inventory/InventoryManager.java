package edu.kit.pms.im.inventory;

import edu.kit.pms.im.domain.MicroserviceError;
import edu.kit.pms.im.domain.ResourceSet;

public interface InventoryManager {
	
	public ResourceSet getResourceSet(int id);
	
	public ResourceSet addResourceSet(ResourceSet resourceSet);
	
	public boolean removeResourceSet(int id);
	
	public boolean changeAmountOfResource(ResourceSet resourceSet) throws MicroserviceError ;
	
	

}
