package edu.kit.pms.im.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Resource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;

	public Resource (@JsonProperty("id") int id, @JsonProperty("name") String name) {
		this.id = id;
		this.name = name;
	}
	
	@JsonIgnore
	public Resource () {
		
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
