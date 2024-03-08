package edu.kit.pms.im.domain;

import java.io.Serializable;

public record ResourceImpl(int id, String name) implements Serializable, Resource {
	

}
