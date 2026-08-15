package com.xanemi.onemace.commands;

import com.xanemi.onemace.OneMacePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

public class GiveMaceCommand implements CommandExecutor {
    private final OneMacePlugin plugin;

    public GiveMaceCommand(OneMacePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        // Give the player one mace regardless
        ItemStack mace = new ItemStack(Material.NETHERITE_AXE, 1);
        ItemMeta meta = mace.getItemMeta();
        meta.setDisplayName("§6Mace");
        meta.setLore(List.of("§7The one and only Mace"));
        meta.getPersistentDataContainer().set(OneMacePlugin.MACE_KEY, PersistentDataType.BYTE, (byte) 1);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.setUnbreakable(true);
        mace.setItemMeta(meta);

        player.getInventory().addItem(mace);
        plugin.getServer().getScheduler().runTask(plugin, () -> plugin.getServer().getPluginManager().callEvent(new org.bukkit.event.player.PlayerJoinEvent(player, "")));
        player.sendMessage("§aGave you a Mace. Only one will be kept.");
        return true;
    }
}
