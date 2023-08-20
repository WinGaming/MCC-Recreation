package mcc.config;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import mcc.indicator.ParticleIndicator;

public class PositionSelector implements ConfigSelector<Location> {
    
    private Location location;

    @Override
    public Location build() {
        return this.location;
    }

    @Override
    public void displayTick(Player player) {
        if (this.location != null) {
            Vector direction = this.location.getDirection().clone().normalize().multiply(5);
            Location eyeLocation = this.location.clone().add(0, player.getEyeHeight(), 0);
            ParticleIndicator.drawLine(player, eyeLocation.clone(), eyeLocation.clone().add(direction), .1f, Color.LIME);
            ParticleIndicator.highlightLocation(player, location, Color.GRAY);
        }
    }

    @Override
    public void onSneakChange(PlayerToggleSneakEvent event) {
        this.location = event.getPlayer().getLocation().clone();
    }
}
