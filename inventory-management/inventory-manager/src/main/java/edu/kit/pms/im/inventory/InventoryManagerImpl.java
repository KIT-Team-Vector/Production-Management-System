package edu.kit.pms.im.inventory;

import edu.kit.pms.im.domain.InventoryManagementError;
import edu.kit.pms.im.domain.Resource;
import edu.kit.pms.im.domain.ResourceSet;
import edu.kit.pms.im.domain.ResourceSetRepository;

public class InventoryManagerImpl implements InventoryManager {

	private ResourceSetRepository resourceSetRepository;

	public InventoryManagerImpl(ResourceSetRepository resourceSetRepository) {
		super();
		this.resourceSetRepository = resourceSetRepository;
	}

	@Override
	public ResourceSet getResourceSet(int id) {
		return resourceSetRepository.get(id);
	}

	public synchronized ResourceSet addResourceSet(ResourceSet resourceSet) throws InventoryManagementError {
		ResourceSet resourceSetInRepository = resourceSetRepository.get(resourceSet.resource().id());
		if (resourceSetInRepository != null
				&& resourcesWithSameNames(resourceSetInRepository.resource(), resourceSet.resource())) {
			increaseResourceSet(resourceSet);
			return resourceSetRepository.get(resourceSet.resource().id());
		} else {
			return resourceSetRepository.add(resourceSet.resource().name(), resourceSet.amount());
		}
	}

	@Override
	public synchronized boolean deleteResourceSet(int id) {
		return resourceSetRepository.delete(id);
	}

	@Override
	public synchronized  boolean increaseResourceSet(ResourceSet resourceSet) throws InventoryManagementError {
		boolean success = resourceSetRepository.updateAmount(resourceSet.resource().id(),
				Math.abs(resourceSet.amount()));
		return success;

	}

	@Override
	public synchronized boolean decreaseResourceSet(ResourceSet resourceSet) throws InventoryManagementError {
		boolean success = resourceSetRepository.updateAmount(resourceSet.resource().id(),
				-Math.abs(resourceSet.amount()));
		return success;

	}

	private boolean resourcesWithSameNames(Resource resource1, Resource resource2) {
		if (resource1 == null || resource2 == null) {
			return false;
		}
		return resource1.name().equals(resource2.name());
	}

}
