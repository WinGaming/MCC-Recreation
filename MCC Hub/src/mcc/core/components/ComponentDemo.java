package mcc.core.components;

import mcc.core.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.GlowSquid;

public class ComponentDemo implements Component {

    private GlowSquid squid;

    @Override
    public void init() {
        Location loc = new Location(Bukkit.getWorld("world"), 268, 35, 248);
        GlowSquid squid = loc.getWorld().spawn(loc, GlowSquid.class);
        squid.setInvulnerable(true);
        squid.setAI(false);
        squid.setSilent(true);
        squid.setGravity(false);
        squid.setCollidable(false);
        squid.setGlowing(true);
        squid.setCustomNameVisible(true);
        squid.setCustomName("Glow Squid");

        this.squid = squid;
    }

    @Override
    public void destroy() {
        squid.remove();
    }
}
