package me.cheezburga.skwe;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.sk89q.worldedit.LocalSession;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

import static me.cheezburga.skwe.api.utils.Utils.pluginPrefix;

@SuppressWarnings("unused")
public class SkWE extends JavaPlugin {

    private static SkWE instance;
    public static boolean HAS_FAWE;
    private SkriptAddon skriptAddon;
    private LocalSession localSession;

    @SuppressWarnings("deprecation")
    @Override
    public void onEnable() {
        instance = this;

        try {
            Bukkit.getConsoleSender().sendMessage(pluginPrefix + ChatColor.GREEN + " Enabling SkWE...");
            skriptAddon = Skript.registerAddon(this).setLanguageFileDirectory("lang");
            Plugin worldEdit = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
            if (worldEdit != null) {
                Bukkit.getConsoleSender().sendMessage(pluginPrefix + ChatColor.GREEN + " Found WorldEdit, checking for FAWE...");
                try {
                    Class.forName("com.fastasyncworldedit.core.Fawe");
                    HAS_FAWE = true;
                    localSession = new LocalSession();
                    Bukkit.getConsoleSender().sendMessage(pluginPrefix + ChatColor.GREEN + " Found FAWE!");
                } catch (ClassNotFoundException e) {
                    HAS_FAWE = false;
                    Bukkit.getConsoleSender().sendMessage(pluginPrefix + ChatColor.RED + " Couldn't find FAWE, syntax will still be enabled but might be more limited.");
                }
                skriptAddon.loadClasses("me.cheezburga.skwe.elements");
            }
            Bukkit.getConsoleSender().sendMessage(pluginPrefix + ChatColor.GREEN + " Finished enabling SkWE!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // syntax enabled/disabled stuff
        // metrics
    }

    public static SkWE getInstance() {
        return instance;
    }

    public SkriptAddon getAddonInstance() {
        return skriptAddon;
    }

    public LocalSession getLocalSession() {
        return localSession;
    }
}