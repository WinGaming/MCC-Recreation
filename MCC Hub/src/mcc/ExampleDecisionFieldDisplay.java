package mcc;

import org.bukkit.Bukkit;

import mcc.decisiondome.DecisionFieldDisplay;

public class ExampleDecisionFieldDisplay implements DecisionFieldDisplay {
    
    @Override
    public void reset() {
        Bukkit.broadcastMessage("Reset display");
    }

    @Override
    public void startRolling() {
        Bukkit.broadcastMessage("Start rolling display");
    }

    @Override
    public void displayGame(String gameKey) {
        Bukkit.broadcastMessage("Display game: " + gameKey);
    }
}
