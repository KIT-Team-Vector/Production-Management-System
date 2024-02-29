package edu.kit.pms.im.domain;

import java.util.Collection;

public interface ResourceSetRepository {
	
	public Collection<ResourceSet> getAll();
	
	public ResourceSet get(int id);
	
	public boolean delete(int id);
	
	public ResourceSet add(String name, int amount);
	
	public boolean updateAmount(int id, int deltaAmount) throws MicroserviceError;
	
}
