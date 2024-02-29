package edu.kit.pms.im.common.concepts;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.pms.im.domain.Resource;
import edu.kit.pms.im.domain.ResourceSet;

public record ResourceSetImpl(ResourceImpl resource, int amount)
		implements ResourceSet, Serializable {

	private static final long serialVersionUID = 1L;
}
