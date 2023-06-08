package mcc;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import mcc.decisiondome.DecisionField;
import mcc.decisiondome.display.DecisionFieldDisplay;

public class ExampleDecisionFieldDisplay extends DecisionFieldDisplay {
    
    public ExampleDecisionFieldDisplay(DecisionField field) {
        super(field);
    }

    @Override
    public void reset() {
        // Bukkit.broadcastMessage("Reset display");
        this.getField().getBlockLocations()[0].getBlock().getLocation().clone().add(0, 5, 0).getBlock().setType(Material.RED_WOOL);
    }

    @Override
    public void startRolling() {
        // Bukkit.broadcastMessage("Start rolling display");
        this.getField().getBlockLocations()[0].getBlock().getLocation().clone().add(0, 5, 0).getBlock().setType(Material.YELLOW_WOOL);
    }

    @Override
    public void displayGame(String gameKey) {
        Bukkit.broadcastMessage("Display game: " + gameKey);
        this.getField().getBlockLocations()[0].getBlock().getLocation().clone().add(0, 5, 0).getBlock().setType(Material.WHITE_WOOL);
    }
}
