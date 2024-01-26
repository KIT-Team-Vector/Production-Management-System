package edu.kit.pms.mm.core;

public record ResourceSet(Resource resource, int amount) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceSet that = (ResourceSet) o;

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
