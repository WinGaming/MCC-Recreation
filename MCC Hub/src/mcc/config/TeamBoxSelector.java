package mcc.config;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import mcc.utils.Pair;

public class TeamBoxSelector implements ConfigSelector<Pair<Pair<Location, Location>, Location>> {

    private AreaSelector areaSelector;
    private PositionSelector positionSelector;

    private boolean finishedArea = false;

    public TeamBoxSelector() {
        this.areaSelector = new AreaSelector();
        this.positionSelector = new PositionSelector();
    }

    @Override
    public Pair<Pair<Location, Location>, Location> build() {
        return new Pair<>(this.areaSelector.build(), this.positionSelector.build());
    }

    @Override
    public void displayTick(Player player) {
        this.areaSelector.displayTick(player);
        this.positionSelector.displayTick(player);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (!finishedArea) {
            this.areaSelector.onBlockBreak(event);
        } else {
            this.positionSelector.onBlockBreak(event);
        }
    }

    @Override
    public void onSneakChange(PlayerToggleSneakEvent event) {
        if (!finishedArea) {
            this.areaSelector.onSneakChange(event);
        } else {
            this.positionSelector.onSneakChange(event);
        }
    }

    @Override
    public boolean nextStep() {
        if (!finishedArea) {
            this.finishedArea = true;
            return true;
        }
        
        return false;
    }
}
