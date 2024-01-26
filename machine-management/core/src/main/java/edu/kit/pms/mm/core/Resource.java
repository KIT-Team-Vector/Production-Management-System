package edu.kit.pms.mm.core;

public record Resource(int id, String name) {

    public Resource(int id) {
        this(id, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        return id == resource.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
