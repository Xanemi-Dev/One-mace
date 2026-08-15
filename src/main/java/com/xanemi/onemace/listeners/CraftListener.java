package com.xanemi.onemace.listeners;

import com.xanemi.onemace.OneMacePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public class CraftListener implements Listener {
    private final OneMacePlugin plugin;

    public CraftListener(OneMacePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        ItemStack result = event.getRecipe() == null ? null : event.getRecipe().getResult();
        if (result == null) return;

        if (plugin.isMace(result)) {
            if (plugin.hasBeenCrafted()) {
                // Someone already crafted the Mace previously
                player.sendMessage("§cA Mace has already been crafted on this server. You cannot craft another.");
                event.setCancelled(true);
            } else {
                // Allow craft, then mark it as crafted by this player
                // Note: the actual item will be given after this event completes; mark now
                plugin.markCrafted(player.getUniqueId());
                player.sendMessage("§aYou have crafted the one Mace for this server.");
            }
        }
    }
}
