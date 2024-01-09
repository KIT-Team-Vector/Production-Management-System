package domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ressource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;

	public Ressource (@JsonProperty("id") int id, @JsonProperty("name") String name) {
		this.id = id;
		this.name = name;
	}
	
	@JsonIgnore
	public Ressource () {
		
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
