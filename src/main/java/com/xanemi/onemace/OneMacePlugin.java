package com.xanemi.onemace;

import org.bukkit.NamespacedKey;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.NamespacedKey;

import java.util.List;
import java.util.UUID;

public final class OneMacePlugin extends JavaPlugin {
    public static NamespacedKey MACE_KEY;
    public static final String CONFIG_CRAFTED = "crafted";
    public static final String CONFIG_OWNER = "owner";

    @Override
    public void onEnable() {
        MACE_KEY = new NamespacedKey(this, "one_mace");
        saveDefaultConfig();

        // Register listeners and command
        getServer().getPluginManager().registerEvents(new com.xanemi.onemace.listeners.CraftListener(this), this);
        getCommand("givemace").setExecutor(new com.xanemi.onemace.commands.GiveMaceCommand(this));

        // Register the custom crafting recipe for the Mace
        ItemStack mace = createMaceItem();
        NamespacedKey recipeKey = new NamespacedKey(this, "one_mace_recipe");
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, mace);
        recipe.shape(" N ", " N ", " S ");
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('S', Material.STICK);
        try {
            getServer().addRecipe(recipe);
            getLogger().info("Registered One Mace crafting recipe.");
        } catch (IllegalStateException e) {
            getLogger().warning("Failed to register recipe: " + e.getMessage());
        }

        getLogger().info("OneMace enabled: only one Mace can be crafted on this server.");
    }

    @Override
    public void onDisable() {
        getLogger().info("OneMace disabled.");
    }

    public ItemStack createMaceItem() {
        ItemStack mace = new ItemStack(Material.NETHERITE_AXE, 1);
        ItemMeta meta = mace.getItemMeta();
        meta.setDisplayName("§6Mace");
        meta.setLore(List.of("§7The one and only Mace"));
        meta.getPersistentDataContainer().set(MACE_KEY, PersistentDataType.BYTE, (byte) 1);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.setUnbreakable(true);
        mace.setItemMeta(meta);
        return mace;
    }

    public boolean isMace(ItemStack item) {
        if (item == null) return false;
        if (item.getType() == Material.AIR) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        Byte has = meta.getPersistentDataContainer().get(MACE_KEY, PersistentDataType.BYTE);
        return has != null && has == 1;
    }

    public boolean hasBeenCrafted() {
        return getConfig().getBoolean(CONFIG_CRAFTED, false);
    }

    public String getOwnerString() {
        return getConfig().getString(CONFIG_OWNER, "");
    }

    public void markCrafted(UUID owner) {
        getConfig().set(CONFIG_CRAFTED, true);
        if (owner != null) getConfig().set(CONFIG_OWNER, owner.toString());
        saveConfig();
    }
}
