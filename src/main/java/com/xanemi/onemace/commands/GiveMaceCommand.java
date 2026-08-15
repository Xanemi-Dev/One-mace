package com.xanemi.onemace.commands;

import com.xanemi.onemace.OneMacePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

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

        if (plugin.hasBeenCrafted()) {
            player.sendMessage("§cA Mace has already been crafted on this server. /givemace is disabled.");
            return true;
        }

        // Give the player the Mace and mark as crafted by them
        ItemStack mace = plugin.createMaceItem();
        player.getInventory().addItem(mace);
        plugin.markCrafted(player.getUniqueId());
        player.sendMessage("§aGave you the one Mace for this server.");
        return true;
    }
}
