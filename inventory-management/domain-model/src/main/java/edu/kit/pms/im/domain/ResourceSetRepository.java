package domain;

import java.util.Collection;
import java.util.List;

public interface Inventory {
	
	public void addRessourceSet(ResourceSet ressourceSet);
	
	public void addRessourceSets(Collection<ResourceSet> ressourceSets);
	
	public void deleteRessourceSet(String id);
	
	public Collection<ResourceSet> getAllRessourceSets();
	
	public ResourceSet getRessourceSetById(String id);
	
	public Collection<ResourceSet> getRessourceSetByName(String name);
	

}
