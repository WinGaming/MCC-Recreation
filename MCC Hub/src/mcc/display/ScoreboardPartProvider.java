package mcc.display;

import java.util.UUID;

import mcc.utils.Pair;

public interface ScoreboardPartProvider {
	
	Pair<String[], Long> getLines(UUID viewer);
	
}
