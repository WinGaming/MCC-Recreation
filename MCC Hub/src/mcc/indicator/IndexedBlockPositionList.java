package mcc.indicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

import mcc.utils.Vector3i;

public class IndexedBlockPositionList {
	
	private Map<Integer, Map<Integer, List<Integer>>> positionList;
	
	public IndexedBlockPositionList(List<Location> locations) {
		this.positionList = new HashMap<>();
		
		for (Location location : locations) {
			Map<Integer, List<Integer>> yMap = positionList.getOrDefault(location.getBlockX(), new HashMap<>());
			List<Integer> zList = yMap.getOrDefault(location.getBlockY(), new ArrayList<>());
			
			if (!zList.contains(location.getBlockZ())) zList.add(location.getBlockZ());
			
			yMap.put(location.getBlockY(), zList);
			positionList.put(location.getBlockX(), yMap);
		}
	}
	
	public List<Vector3i> getBlockPositions() {
		List<Vector3i> positions = new ArrayList<>();
		
		this.positionList.forEach((x, yMap) -> {
			yMap.forEach((y, zList) -> {
				zList.forEach(z -> {
					positions.add(new Vector3i(x, y, z));
				});
			});
		});
		
		return positions;
	}
	
	public boolean hasBlockAt(int x, int y, int z) {
		return positionList.containsKey(x) && positionList.get(x).containsKey(y) && positionList.get(x).get(y).contains(z);
	}
}
