package edu.kit.pms.im.domain;

import java.io.Serializable;

public record ResourceSetImpl(ResourceImpl resource, int amount)
		implements ResourceSet, Serializable {

}
