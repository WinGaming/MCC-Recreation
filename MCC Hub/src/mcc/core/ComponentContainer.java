package mcc.core;

import java.util.ArrayList;
import java.util.List;

public abstract class ComponentContainer {

    private final List<Component> components;

    public ComponentContainer(Component ... components) {
        this.components = new ArrayList<>();
        this.components.addAll(List.of(components));

        this.components.forEach(Component::init);
    }

    public void tick(long now) {
        this.components.forEach(comp -> comp.tick(now));
    }

    public void destroy() {
        this.components.forEach(Component::destroy);
    }
}
