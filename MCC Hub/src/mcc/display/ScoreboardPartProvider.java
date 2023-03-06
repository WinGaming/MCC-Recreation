package mcc.display;

import java.util.UUID;

public interface ScoreboardPartProvider {
	
	String[] getLines(UUID viewer);
	
}
