package edu.kit.pms.im.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceSet {
	
	private Resource ressource;

	private int amount;

	public ResourceSet (@JsonProperty("ressource") Resource ressource, @JsonProperty("amount") int amount) {
		this.ressource = ressource;
		this.amount = amount;
	}
	
	public Resource getRessource() {
		return ressource;
	}

	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}


}
