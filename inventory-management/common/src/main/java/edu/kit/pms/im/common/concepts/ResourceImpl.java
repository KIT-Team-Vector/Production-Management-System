package edu.kit.pms.im.common.concepts;

import java.io.Serializable;



import edu.kit.pms.im.domain.Resource;

public record ResourceImpl(int id, String name) implements Serializable, Resource {
	
	private static final long serialVersionUID = 1L;

}
