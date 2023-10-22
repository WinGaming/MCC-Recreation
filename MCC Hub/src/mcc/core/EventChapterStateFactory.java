package mcc.core;

@FunctionalInterface
public interface EventChapterStateFactory<EventState extends Enum<EventState>> {

    EventChapterState<EventState> create();

}
