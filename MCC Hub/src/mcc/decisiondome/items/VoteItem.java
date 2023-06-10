package mcc.decisiondome.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface VoteItem {
    
    ItemStack createInstance();

    void onInteraction(Player player);

}
