package edu.kit.pms.im.inventory;

import edu.kit.pms.im.domain.ResourceSetRepository;
import edu.kit.pms.im.domain.ResourceSet;

public class InventoryManagerImpl implements InventoryManager{
	
	
	private ResourceSetRepository inventory;
	
	
	public InventoryManagerImpl(ResourceSetRepository inventory) {
		super();
		this.inventory = inventory;
	}


	public void addResourceSet(ResourceSet resourceSet) {
	}


	@Override
	public ResourceSet getResourceSet(int id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ResourceSet removeResourceSet(ResourceSet resourceSet) {
		// TODO Auto-generated method stub
		return null;
	}

}
