package mcc.display;

import java.util.List;
import java.util.function.Function;

import org.bukkit.ChatColor;

import mcc.teams.Team;
import mcc.teams.TeamTemplate;
import mcc.utils.WidthLimitedString;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;

public class TablistDisplay {
    
    private Function<Integer, String> indexStringBuilder;

    public TablistDisplay() {
        this.indexStringBuilder = (index) -> (index + 1) + ".";
    }

    public PacketPlayOutPlayerListHeaderFooter generatePacket(List<Team> sortedTeams, int totalWidth) {
        final String SEPERATOR_LINE = "\n" + ChatColor.RED + ChatColor.STRIKETHROUGH + SpaceFont.getSpaceString(totalWidth);
        
        String headerTemplate = "";
        
        // Add header
        headerTemplate += ChatColor.GOLD + "MCC CHAMPIONSHIP" + ChatColor.RESET;
        headerTemplate += "\n" + ChatColor.YELLOW + "Presented by " + ChatColor.RED + "@Noxcrew" + ChatColor.YELLOW + " & " + ChatColor.AQUA + "@Smajor1995";

        // Add seperator line
        headerTemplate += SEPERATOR_LINE;

        // Add game info
        headerTemplate += "\n" + ChatColor.WHITE + "Grid Runners - GAME COINS";

        // Add empty line
        headerTemplate += "\n";

        // Add team info
        int longestIndexWidth = 0;
        for (int i = 0; i < sortedTeams.size(); i++) {
            longestIndexWidth = Math.max(longestIndexWidth, SpaceFont.getWidthOf(this.indexStringBuilder.apply(i)));
        }

        for (int i = 0; i < sortedTeams.size(); i++) {
            Team team = sortedTeams.get(i);

            String indexString = this.indexStringBuilder.apply(i);
            TeamTemplate template = team.getTemplate();
            
            String coinString = "1000~";
            int coinStringLength = SpaceFont.getWidthOf(coinString);

            String teamString = indexString + " " + template.getIcon() + " " + template.getColor() + template.getName() + ChatColor.RESET;
            int teamStringLength = SpaceFont.getWidthOf(teamString);

            String nameString = teamString + SpaceFont.getSpaceString(totalWidth - teamStringLength - coinStringLength) + coinString;

            int indexWidth = SpaceFont.getWidthOf(indexString + " ");
            String membersString = SpaceFont.getSpaceString(indexWidth) + WidthLimitedString.buildString(totalWidth - indexWidth, team.getTempNameVar().toArray(new String[team.getTempNameVar().size()]));

            headerTemplate += "\n" + nameString + "\n" + template.getColor() + membersString + ChatColor.RESET;

            if (i < sortedTeams.size() - 1) {
                headerTemplate += "\n";
            }
        }

        headerTemplate += "\n";
        headerTemplate += SEPERATOR_LINE;
        
        return new PacketPlayOutPlayerListHeaderFooter(IChatBaseComponent.literal(headerTemplate), IChatBaseComponent.empty());
    }
}
