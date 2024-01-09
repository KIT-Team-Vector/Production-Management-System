package domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RessourceSet {
	
	private Ressource ressource;

	private int amount;

	public RessourceSet (@JsonProperty("ressource") Ressource ressource, @JsonProperty("amount") int amount) {
		this.ressource = ressource;
		this.amount = amount;
	}
	
	public Ressource getRessource() {
		return ressource;
	}

	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}


}
