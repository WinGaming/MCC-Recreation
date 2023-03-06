package mcc.display;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
	
    private ScoreboardObjective objective;
    // private int[] lastPartSizes;
    // private int lastPartCount;

	private IChatBaseComponent title;
	
	private ScoreboardPartProvider[] parts;

	public CachedScoreboardTemplate(IChatBaseComponent title, ScoreboardPartProvider[] parts) {
		this.title = title;
		this.parts = parts;
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

		// TODO: Check if the parts need to update

		if (this.parts.length == 0) {
			PacketPlayOutScoreboardScore scorePacket = new PacketPlayOutScoreboardScore(Action.CHANGE, objectiveId, " ", 0);
			((CraftPlayer) player).getHandle().connection.send(scorePacket);
		}

		String lastSpaceString = " ";
		List<String> allLines = new ArrayList<>();
		for (ScoreboardPartProvider part : parts) {
			String lines[] = part.getLines(player.getUniqueId());
			for (String line : lines) allLines.add(line);
			allLines.add(lastSpaceString);
			lastSpaceString += " ";
		}

		// All lines but the last one, because the last one is a spacer
		for (int i = 0; i < allLines.size() - 1; i++) {
			String line = allLines.get(i);

			PacketPlayOutScoreboardScore scorePacket = new PacketPlayOutScoreboardScore(Action.CHANGE, objectiveId, line, allLines.size() - 1 - i);
			((CraftPlayer) player).getHandle().connection.send(scorePacket);
		}
	}
}
