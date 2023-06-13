package mcc.decisiondome.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/** Represents an item that can be used by players while voting */
public interface VoteItem {
    
    /**
     * Returns a new instance of the item, that can be given to a player.
     * @return a new instance of the item
     */
    ItemStack createInstance();

    /**
     * Called when a player interacted with the item.
     * This method will only be called if the interaction happened
     * at a time the player was allowed to vote.
     * @param player the player who interacted with the item
     */
    void onInteraction(Player player);

}
