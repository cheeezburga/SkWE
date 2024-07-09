package me.cheezburga.skwe;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.extension.platform.Platform;
import me.cheezburga.skwe.api.utils.UpdateChecker;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SkWE extends JavaPlugin {

    private static SkWE instance;
    public static boolean HAS_FAWE;
    private SkriptAddon skriptAddon;

    @Override
    public void onEnable() {
        instance = this;

        try {
            skriptAddon = Skript.registerAddon(this).setLanguageFileDirectory("lang");
            skriptAddon.loadClasses("me.cheezburga.skwe.elements");
            Utils.log("&aLooking for WorldEdit or FAWE...");
            Plugin worldEdit = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
            if (worldEdit != null) {
                try {
                    Class.forName("com.fastasyncworldedit.core.Fawe");
                    HAS_FAWE = true;
                    Utils.log("&a  - Found FAWE!");
                } catch (ClassNotFoundException e) {
                    HAS_FAWE = false;
                    Utils.log("&a  - Found WorldEdit! Syntax will still be enabled, but might be more limited than a server using FAWE.");
                }
                Utils.log("&a  - Finished enabling SkWE!");
                if (HAS_FAWE)
                    preLoadRelighterFactory();
            } else { // should never get to this, as server shouldn't try enabling if the dependency isn't found
                Utils.log("&c  - Couldn't find WorldEdit or FAWE! Disabling SkWE...");
                getServer().getPluginManager().disablePlugin(this);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load SkWE: " + e);
        }
        loadMetrics();
        //checkUpdate(getDescription().getVersion());
    }

    private void loadMetrics() {
        Metrics metrics = new Metrics(this, 22535);
        metrics.addCustomChart(new Metrics.SimplePie("skriptVersion", () -> Skript.getVersion().toString()));
    }

    private void checkUpdate(String version) {
        UpdateChecker.checkForUpdate(version);
    }

    private void preLoadRelighterFactory() {
        Platform platform = WorldEdit.getInstance().getPlatformManager().queryCapability(Capability.WORLD_EDITING);
        if (platform != null) {
            Utils.log("&aTrying to preload FAWE's relighter factory...");
            try {
                Method relighterMethod = platform.getClass().getMethod("getRelighterFactory");
                Object relighterFactory = relighterMethod.invoke(platform);
                Utils.log("&a  - Found FAWE's relighter factory!");
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                Utils.log("&c  - Failed to load FAWE's relighter with exception " + e.getMessage());
            }
        }
    }

    public static SkWE getInstance() {
        return instance;
    }

    public SkriptAddon getAddonInstance() {
        return skriptAddon;
    }

}