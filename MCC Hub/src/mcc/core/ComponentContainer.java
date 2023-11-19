package mcc.core;

import java.util.ArrayList;
import java.util.List;

public class ComponentContainer {

    private final List<Component> components;

    public ComponentContainer(Component ... components) {
        this.components = new ArrayList<>();
        this.components.addAll(List.of(components));

        this.components.forEach(Component::init);
    }

    public void addComponent(Component component) {
        this.components.add(component);
        component.init();
    }

    public void addAll(Component ... components) {
        for (Component component : components) {
            this.addComponent(component);
        }
    }

    public void removeComponent(Component component) {
        this.components.remove(component);
        component.destroy();
    }

    public void clear() {
        this.components.forEach(Component::destroy);
        this.components.clear();
    }

    public void onChapterStateChange() {
        this.components.forEach(Component::onChapterStateChange);
    }

    public void tick(long now) {
        this.components.forEach(comp -> comp.tick(now));
    }

    public void destroy() {
        this.components.forEach(Component::destroy);
    }
}
