package edu.kit.pms.im.common.controllers;

import edu.kit.pms.im.domain.ResourceSet;

public interface ExternalInterfaceController {

	public ResourceSet getResourceSet(int id);

	public ResourceSet addResourceSet(ResourceSet resourceSet);

	public boolean removeResourceSet(int id);

	public void changeAmountOfResource(Long key, ResourceSet resourceSet);

}
