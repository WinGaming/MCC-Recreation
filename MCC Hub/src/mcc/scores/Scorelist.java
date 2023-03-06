package mcc.scores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mcc.utils.Pair;

public class Scorelist<T extends Score<T, ?>> {
	
	private Map<UUID, T> scores;

	public Scorelist() {
		this.scores = new HashMap<>();
	}

	public void setScore(UUID uuid, T score) {
		scores.put(uuid, score);
	}
	
	public List<Pair<UUID, T>> sortScores() {
		List<Pair<UUID, T>> sorted = new ArrayList<>();
		for (UUID uuid : scores.keySet()) {
			sorted.add(new Pair<>(uuid, scores.get(uuid)));
		}
		sorted.sort((pairA, pairB) -> pairA.getB().compareTo(pairB.getB()));
		return sorted;
	}

	public int size() {
		return scores.size();
	}
}
