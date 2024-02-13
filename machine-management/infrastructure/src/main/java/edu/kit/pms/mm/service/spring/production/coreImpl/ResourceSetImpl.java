package edu.kit.pms.mm.service.spring.production.coreImpl;

import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;

public record ResourceSetImpl(Resource resource, int amount) implements ResourceSet {

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
