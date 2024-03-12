package edu.kit.pms.mm.infrastructure.production.coreImpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.kit.pms.mm.core.ResourceSet;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResourceSetImpl(ResourceImpl resource, int amount) implements ResourceSet {

    @Override
    public String toString() {
        return amount + "x " + resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceSetImpl that = (ResourceSetImpl) o;

        if (amount != that.amount) return false;
        return resource.equals(that.resource);
    }

    @Override
    public int hashCode() {
        int result = resource.hashCode();
        result = 31 * result + amount;
        return result;
    }
}
