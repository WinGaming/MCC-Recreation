package mcc.core.minigame.battlebox;

import mcc.core.team.Team;
import org.bukkit.Location;
import org.bukkit.Material;

public class BattleBoxTeam {

    private Material teamBlock;

    public BattleBoxTeam(Material teamBlock) {
        this.teamBlock = teamBlock;
    }

    public Material getTeamBlock() {
        return teamBlock;
    }
}
