package mcc.core;

import mcc.core.event.EventChapter;

import java.util.List;

public class ComponentCreationRegistry {

    private static List<ComponentFactory> factories;

    public static void register(ComponentFactory factory) {
        factories.add(factory);
    }

    public static void unregister(ComponentFactory factory) {
        factories.remove(factory);
    }

    public static List<Component> createComponentsFor(EventChapter chapter) {
        return factories.stream().map(factory -> factory.createComponentsFor(chapter)).flatMap(List::stream).toList();
    }
}
