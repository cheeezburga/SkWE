package me.cheezburga.skwe;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.sk89q.worldedit.LocalSession;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

import static me.cheezburga.skwe.api.utils.Utils.pluginPrefix;

@SuppressWarnings("unused")
public class SkWE extends JavaPlugin {

    private static SkWE instance;
    private SkriptAddon skriptAddon;
    private LocalSession localSession;

    @SuppressWarnings("deprecation")
    @Override
    public void onEnable() {
        instance = this;

        try {
            skriptAddon = Skript.registerAddon(this).setLanguageFileDirectory("lang");
            skriptAddon.loadClasses("me.cheezburga.skwe.elements");
            Bukkit.getConsoleSender().sendMessage(pluginPrefix + ChatColor.GREEN + " Enabled SkWE!");

            if (Bukkit.getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit")) {
                Bukkit.getConsoleSender().sendMessage(pluginPrefix + ChatColor.GREEN + " Found FAWE!");
                localSession = new LocalSession();
            } else Bukkit.getConsoleSender().sendMessage(pluginPrefix + ChatColor.RED + " Couldn't find FAWE!");
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