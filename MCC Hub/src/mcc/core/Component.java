package mcc.core;

/**
 * A component can be added to a container to add functionality.
 */
public interface Component {

    /**
     * Called when the container is created.
     */
    default void init() {}

    /**
     * Called every tick.
     * @param now The current time in milliseconds
     */
    default void tick(long now) {}

    /**
     * Called when the container is destroyed.
     */
    default void destroy() {}

}
