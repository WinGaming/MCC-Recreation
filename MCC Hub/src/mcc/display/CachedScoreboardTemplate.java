package mcc.display;

import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import mcc.utils.Pair;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore;
import net.minecraft.server.ScoreboardServer.Action;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay;

public class CachedScoreboardTemplate {
	
	public class ScoreboardPartCache {
		public String[] lines;
		public long lastUpdate;
	}

    private ScoreboardObjective objective;
	
	private IChatBaseComponent title;
	private ScoreboardPartProvider[] parts;

    private ScoreboardPartCache[] partCaches;

	public CachedScoreboardTemplate(IChatBaseComponent title, ScoreboardPartProvider[] parts) {
		this.title = title;
		this.parts = parts;

		this.partCaches = new ScoreboardPartCache[parts.length];
	}

	public void show(Player player) {
		final String objectiveId = "objective_id";
		
		if (this.objective == null) {
			this.objective = new ScoreboardObjective(new Scoreboard(), objectiveId, IScoreboardCriteria.DUMMY, title, EnumScoreboardHealthDisplay.INTEGER);
		}

		PacketPlayOutScoreboardObjective objectivePacket = new PacketPlayOutScoreboardObjective(objective, PacketPlayOutScoreboardObjective.METHOD_ADD);
		((CraftPlayer) player).getHandle().connection.send(objectivePacket);
		
		PacketPlayOutScoreboardDisplayObjective displayPacket = new PacketPlayOutScoreboardDisplayObjective(Scoreboard.getDisplaySlotByName("sidebar"), objective);
		((CraftPlayer) player).getHandle().connection.send(displayPacket);

		if (this.parts.length == 0) {
			// Remove all current lines
			for (int i = 0; i < this.partCaches.length; i++) {
				for (int j = 0; j < this.partCaches[i].lines.length; j++) {
					PacketPlayOutScoreboardScore scorePacket = new PacketPlayOutScoreboardScore(Action.REMOVE, objectiveId, this.partCaches[i].lines[j], 0);
					((CraftPlayer) player).getHandle().connection.send(scorePacket);
				}
			}
			this.partCaches = new ScoreboardPartCache[] {};

			PacketPlayOutScoreboardScore scorePacket = new PacketPlayOutScoreboardScore(Action.CHANGE, objectiveId, " ", 0);
			((CraftPlayer) player).getHandle().connection.send(scorePacket);
		}

		int currentScore = 0;
		String lastSpaceString = "";
		boolean requireScoreUpdate = this.partCaches.length != this.parts.length;
		for (int forwardPartIndex = 0; forwardPartIndex < this.parts.length; forwardPartIndex++) {
			int partIndex = this.parts.length - 1 - forwardPartIndex;
			ScoreboardPartCache partCache = this.partCaches[partIndex];

			ScoreboardPartProvider partProvider = this.parts[partIndex];
			Pair<String[], Long> linesResult = partProvider.getLines(player.getUniqueId());
			String[] lines = linesResult.getA();

			if (partCache == null || lines.length != partCache.lines.length) {
				requireScoreUpdate = true;
			}

			if (!requireScoreUpdate && partCache != null && partCache.lastUpdate >= linesResult.getB()) {
				currentScore += lines.length + 1;
				lastSpaceString += " ";
				continue;
			}

			// Update cache
			if (partCache == null) {
				partCache = new ScoreboardPartCache();
				this.partCaches[partIndex] = partCache;
			} else {
				for (String line : partCache.lines) {
					PacketPlayOutScoreboardScore scorePacket = new PacketPlayOutScoreboardScore(Action.REMOVE, objectiveId, line, 0);
					((CraftPlayer) player).getHandle().connection.send(scorePacket);	
				}
			}

			partCache.lines = lines;
			partCache.lastUpdate = linesResult.getB();

			// Send lines
			for (int forwardLineIndex = 0; forwardLineIndex < lines.length; forwardLineIndex++) {
				int lineIndex = lines.length - 1 - forwardLineIndex;

				String line = lines[lineIndex];

				final int score = currentScore++;
				PacketPlayOutScoreboardScore scorePacket = new PacketPlayOutScoreboardScore(Action.CHANGE, objectiveId, line, score);
				((CraftPlayer) player).getHandle().connection.send(scorePacket);
			}

			if (forwardPartIndex != this.parts.length - 1) {
				// Send spacer
				final int score = currentScore++;
				PacketPlayOutScoreboardScore scorePacket = new PacketPlayOutScoreboardScore(Action.CHANGE, objectiveId, lastSpaceString += " ", score);
				((CraftPlayer) player).getHandle().connection.send(scorePacket);
			}
		}
	}
}
