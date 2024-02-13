package edu.kit.pms.im.domain;

import java.util.Collection;
import java.util.List;

public interface ResourceSetRepository {
	
	public Collection<ResourceSet> getAll();
	
	public ResourceSet get(int id);
	
	public void delete(int id);
	
	public void add(ResourceSet ressourceSet);
	
	public void updateAmount(int id, int deltaAmount);
	
}
