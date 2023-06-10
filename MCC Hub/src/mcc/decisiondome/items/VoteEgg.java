package mcc.decisiondome.items;

import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VoteEgg implements VoteItem {

    @Override
    public ItemStack createInstance() {
        return new ItemStack(Material.EGG);
    }

    @Override
    public void onInteraction(Player player) {
        Egg egg = player.launchProjectile(Egg.class);
        egg.setCustomName("team_name"); // TODO:
    }
}
