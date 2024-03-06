package edu.kit.pms.mm.infrastructure.production.coreImpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.kit.pms.mm.core.Resource;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResourceSetImpl(Resource resource, int amount) implements edu.kit.pms.mm.core.ResourceSet {

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
