package edu.kit.pms.mm.core;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Machine {
    private final int _id;
    private final List<ResourceSet> _input;
    private final List<ResourceSet> _output;

    public Machine(int id, List<ResourceSet> input, List<ResourceSet> output) throws IllegalArgumentException {
        if (id <= 0) throw new IllegalArgumentException("id must be greater than 0");
        if (output == null) throw new IllegalArgumentException("output must not be null");

        _id = id;
        _input = Objects.requireNonNullElse(input, Collections.emptyList());
        _output = output;
    }

    public List<ResourceSet> produce(List<ResourceSet> providedResources) throws IllegalArgumentException {
        if (Objects.equals(_input, providedResources)) {
            return _output;
        }
        throw new IllegalArgumentException("providedResources must match input");
    }

    public int getId() {
        return _id;
    }

    public List<ResourceSet> getInput() {
        return Collections.unmodifiableList(_input);
    }

    public Collection<Resource> getRequiredResources() {
        return _input.stream().map(ResourceSet::resource).toList();
    }

    public List<ResourceSet> getOutput() {
        return Collections.unmodifiableList(_output);
    }

    public Collection<Resource> getProducedResources() {
        return _output.stream().map(ResourceSet::resource).toList();
    }
}
