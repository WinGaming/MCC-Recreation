package mcc.core.event;

@FunctionalInterface
public interface EventChapterStateFactory<EventState extends Enum<EventState>> {

    EventChapterState<EventState> create();

}
