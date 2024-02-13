package edu.kit.pms.mm.service.rest.coreImpl;

import edu.kit.pms.mm.core.Resource;

public record ResourceImpl(int id, String name) implements Resource {

    public ResourceImpl(int id) {
        this(id, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceImpl resource = (ResourceImpl) o;

        return id == resource.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
