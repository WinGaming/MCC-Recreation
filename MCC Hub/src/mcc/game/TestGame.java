package mcc.game;

import java.util.Random;

import org.bukkit.Bukkit;

public class TestGame extends Game {

    @Override
    public void prepare() {
        Bukkit.broadcastMessage("setup game");
    }

    @Override
    public void teleportPlayers() {

    }

    @Override
    public boolean tick(long now) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setHealth(new Random().nextInt(20) + 1);
        });
        return false;
    }
}
