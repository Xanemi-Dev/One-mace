package com.xanemi.onemace;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.NamespacedKey;

public final class OneMacePlugin extends JavaPlugin {
    public static NamespacedKey MACE_KEY;

    @Override
    public void onEnable() {
        MACE_KEY = new NamespacedKey(this, "one_mace");
        getServer().getPluginManager().registerEvents(new com.xanemi.onemace.listeners.MaceListener(this), this);
        getCommand("givemace").setExecutor(new com.xanemi.onemace.commands.GiveMaceCommand(this));
        getLogger().info("OneMace enabled: players will have at most one Mace.");
    }

    @Override
    public void onDisable() {
        getLogger().info("OneMace disabled.");
    }
}
