package mcc.core;

import mcc.core.event.EventChapter;

import java.util.List;

@FunctionalInterface
public interface ComponentFactory {

    List<Component> createComponentsFor(EventChapter chapter);

}
