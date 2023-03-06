package mcc.scores;

import java.util.Optional;

public interface Score<T extends Score<T, V>, V> extends Comparable<T> {

	public void setScore(V value);
	public V getScore();

	public String toChatText(Optional<T> viewerScore);
	
	public String toScoreboardText(Optional<T> viewerScore);
	
}