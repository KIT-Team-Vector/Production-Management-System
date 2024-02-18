package edu.kit.pms.im.inventory;

import edu.kit.pms.im.domain.MicroserviceError;
import edu.kit.pms.im.domain.ResourceSet;
import edu.kit.pms.im.domain.ResourceSetRepository;

public class InventoryManagerImpl implements InventoryManager {


	private ResourceSetRepository resourceSetRepository;

	public InventoryManagerImpl(ResourceSetRepository resourceSetRepository) {
		super();
		this.resourceSetRepository = resourceSetRepository;
	}


	public ResourceSet addResourceSet(ResourceSet resourceSet)  {
		// ignore id, id are assigned by the data base
		return resourceSetRepository.add(resourceSet.resource().name(), resourceSet.amount());
	}

	@Override
	public ResourceSet getResourceSet(int id) {
		return resourceSetRepository.get(id);
	}

	@Override
	public boolean removeResourceSet(int id) {
		return resourceSetRepository.delete(id);
	}

	@Override
	public boolean changeAmountOfResource(ResourceSet resourceSet) throws MicroserviceError {
		boolean success = resourceSetRepository.updateAmount(resourceSet.resource().id(), resourceSet.amount());	
		return success;

	}

}
