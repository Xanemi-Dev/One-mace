package com.xanemi.onemace.listeners;

import com.xanemi.onemace.OneMacePlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MaceListener implements Listener {
    private final Plugin plugin;
    private final NamespacedKey key;

    public MaceListener(Plugin plugin) {
        this.plugin = plugin;
        this.key = OneMacePlugin.MACE_KEY;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ensureSingleMace(player);
        if (!hasMace(player)) {
            giveMace(player);
            player.sendMessage("&aYou have been given your Mace. You may only hold one.");
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        ItemStack item = event.getItem().getItemStack();
        if (isMace(item)) {
            Player player = (Player) event.getEntity();
            if (hasMace(player)) {
                event.setCancelled(true);
            } else {
                // allow pickup, then enforce single (scheduled next tick)
                plugin.getServer().getScheduler().runTask(plugin, () -> ensureSingleMace(player));
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // small delay to let click finish and then enforce single mace
        if (event.getWhoClicked() instanceof Player) {
            Player p = (Player) event.getWhoClicked();
            plugin.getServer().getScheduler().runTask(plugin, () -> ensureSingleMace(p));
        }
    }

    private boolean isMace(ItemStack item) {
        if (item == null) return false;
        if (item.getType() == Material.AIR) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        Byte has = meta.getPersistentDataContainer().get(key, PersistentDataType.BYTE);
        return has != null && has == 1;
    }

    private void giveMace(Player player) {
        ItemStack mace = new ItemStack(Material.NETHERITE_AXE, 1);
        ItemMeta meta = mace.getItemMeta();
        meta.setDisplayName("§6Mace");
        meta.setLore(List.of("§7The one and only Mace"));
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.setUnbreakable(true);
        mace.setItemMeta(meta);

        player.getInventory().addItem(mace);
    }

    private boolean hasMace(Player player) {
        int count = 0;
        for (ItemStack it : player.getInventory().getContents()) {
            if (isMace(it)) count++;
            if (count >= 1) return true;
        }
        return false;
    }

    private void ensureSingleMace(Player player) {
        int found = 0;
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack it = contents[i];
            if (isMace(it)) {
                found++;
                if (found > 1) {
                    // remove extras
                    player.getInventory().setItem(i, null);
                }
            }
        }
        // If player has none, give one
        if (found == 0) {
            giveMace(player);
        }
        player.updateInventory();
    }
}
