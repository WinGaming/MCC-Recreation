package mcc.indicator;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import mcc.utils.Vector3d;
import mcc.utils.Vector3i;

public class ParticleIndicator {

	private enum BlockCorner {
		TOP_NORTH(new Vector3i(0, 1, 0), new Vector3i(1, 1, 0), new Vector3i(0, 0, -1), new Vector3i(0, 1, -1), new Vector3i(0, 1, 0)),
		TOP_SOUTH(new Vector3i(0, 1, 1), new Vector3i(1, 1, 1), new Vector3i(0, 0, 1), new Vector3i(0, 1, 1), new Vector3i(0, 1, 0)),
		TOP_EAST(new Vector3i(1, 1, 0), new Vector3i(1, 1, 1), new Vector3i(1, 0, 0), new Vector3i(1, 1, 0), new Vector3i(0, 1, 0)),
		TOP_WEST(new Vector3i(0, 1, 0), new Vector3i(0, 1, 1), new Vector3i(-1, 0, 0), new Vector3i(-1, 1, 0), new Vector3i(0, 1, 0)),
		
		BOTTOM_NORTH(new Vector3i(0, 0, 0), new Vector3i(1, 0, 0), new Vector3i(0, -1, -1), new Vector3i(0, 0, -1), new Vector3i(0, -1, 0)),
		BOTTOM_SOUTH(new Vector3i(0, 0, 1), new Vector3i(1, 0, 1), new Vector3i(0, -1, 1), new Vector3i(0, 0, 1), new Vector3i(0, -1, 0)),
		BOTTOM_EAST(new Vector3i(1, 0, 0), new Vector3i(1, 0, 1), new Vector3i(1, -1, 0), new Vector3i(1, 0, 0), new Vector3i(0, -1, 0)),
		BOTTOM_WEST(new Vector3i(0, 0, 0), new Vector3i(0, 0, 1), new Vector3i(-1, -1, 0), new Vector3i(-1, 0, 0), new Vector3i(0, -1, 0)),
		
		CORNER_NORTH_EAST(new Vector3i(1, 0, 0), new Vector3i(1, 1, 0), new Vector3i(0, 0, -1), new Vector3i(1, 0, 0), new Vector3i(1, 0, -1)),
		CORNER_NORTH_WEST(new Vector3i(0, 0, 0), new Vector3i(0, 1, 0), new Vector3i(-1, 0, 0), new Vector3i(-1, 0, -1), new Vector3i(0, 0, -1)),
		CORNER_SOUTH_EAST(new Vector3i(1, 0, 1), new Vector3i(1, 1, 1), new Vector3i(0, 0, 1), new Vector3i(1, 0, 0), new Vector3i(1, 0, 1)),
		CORNER_SOUTH_WEST(new Vector3i(0, 0, 1), new Vector3i(0, 1, 1), new Vector3i(0, 0, 1), new Vector3i(-1, 0, 0), new Vector3i(-1, 0, 1)),
		;
		
		private Vector3i start, end;
		private Vector3i[] deltaToCheck;
		
		private BlockCorner(Vector3i start, Vector3i end, Vector3i ...deltaToCheck) {
			this.start = start;
			this.end = end;
			
			this.deltaToCheck = deltaToCheck;
		}

		public boolean check(IndexedBlockPositionList list, Vector3i source) {
			for (int i = 0; i < deltaToCheck.length; i++) {
				Vector3i delta = deltaToCheck[i];
				if (list.hasBlockAt(source.getX() + delta.getX(), source.getY() + delta.getY(), source.getZ() + delta.getZ())) {
					return false;
				}
			}
			
			return true;
		}
		
		public Vector3i getStart() {
			return start;
		}
		
		public Vector3i getEnd() {
			return end;
		}
	}
	
	private static Location fromVector(World world, Vector3i vector) {
		return new Location(world, vector.getX(), vector.getY(), vector.getZ());
	}

	public static final void highlightArea(Player player, World world, Vector3i cornerA, Vector3i cornerB) {
		Vector3i min = new Vector3i(Math.min(cornerA.getX(), cornerB.getX()), Math.min(cornerA.getY(), cornerB.getY()), Math.min(cornerA.getZ(), cornerB.getZ()));
		Vector3i max = new Vector3i(Math.max(cornerA.getX(), cornerB.getX()), Math.max(cornerA.getY(), cornerB.getY()), Math.max(cornerA.getZ(), cornerB.getZ()));

		



	}

	public static final void highlightBlocks(World world, List<Location> locations) {
		IndexedBlockPositionList blockPositionList = new IndexedBlockPositionList(locations);
		
		for (Vector3i position : blockPositionList.getBlockPositions()) {
			for (BlockCorner corner : BlockCorner.values()) {
				if (corner.check(blockPositionList, position)) {
					drawLine(
							null,
							new Location(world, position.getX() + corner.getStart().getX(), position.getY() + corner.getStart().getY(), position.getZ() + corner.getStart().getZ()),
							new Location(world, position.getX() + corner.getEnd().getX(), position.getY() + corner.getEnd().getY(), position.getZ() + corner.getEnd().getZ()),
							0.1f, Color.FUCHSIA);
				}
			}
		}
	}
	
	public static final void drawLine(Player player, Location start, Location end, float stepSize, Color color) {
		if (stepSize == 0) throw new IllegalStateException("stepSize can not be 0!");
		
		World world = start.getWorld();
		double distance = start.distance(end);
		Vector3d deltaVector = new Vector3d(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ()).normalize();
		
		for (int i = 0; i < Math.floor(distance / stepSize); i++) {
			Location loc = start.clone().add(deltaVector.getX() * stepSize * i, deltaVector.getY() * stepSize * i, deltaVector.getZ() * stepSize * i);
			world.spawnParticle(Particle.REDSTONE, loc, 1, new Particle.DustOptions(color, .5f));
		}
		
		world.spawnParticle(Particle.REDSTONE, end, 1, new Particle.DustOptions(color, .5f));
	}
	
}
