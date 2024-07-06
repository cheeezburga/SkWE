package me.cheezburga.skwe;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
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
            Utils.log("&aLooking for WorldEdit or FAWE...");
            Plugin worldEdit = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
            if (worldEdit != null) {
                Utils.log("&aFound a WorldEdit instance, checking for FAWE...");
                try {
                    Class.forName("com.fastasyncworldedit.core.Fawe");
                    HAS_FAWE = true;
                    Utils.log("&aFound FAWE!");
                } catch (ClassNotFoundException e) {
                    HAS_FAWE = false;
                    Utils.log("&cCouldn't find FAWE, syntax will still be enabled but will be more limited.");
                }
                skriptAddon.loadClasses("me.cheezburga.skwe.elements");
            } else {
                Utils.log("&cCouldn't find WorldEdit or FAWE! Disabling SkWE...");
                getServer().getPluginManager().disablePlugin(this);
            }
            Utils.log("&aFinished enabling SkWE!");
            if (HAS_FAWE)
                preLoadRelighterFactory();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load SkWE: " + e);
        }
        loadMetrics();
        // checkUpdate(getDescription().getVersion());
    }

    private void loadMetrics() {
        Metrics metrics = new Metrics(this, 22535);
        metrics.addCustomChart(new Metrics.SimplePie("skriptVersion", () -> Skript.getVersion().toString()));
    }

    private void checkUpdate(String version) {
        UpdateChecker.checkForUpdate(version);
    }

    private void preLoadRelighterFactory() {
        WorldEditPlugin plugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin != null) {
            Utils.log("&6Looking for FAWE's relighter factory...");
            try {
                Method bukkitImplAdapterMethod = plugin.getClass().getMethod("getBukkitImplAdapter");
                Object bukkitImplAdapter = bukkitImplAdapterMethod.invoke(plugin);

                if (bukkitImplAdapter != null) {
                    Method relighterFactoryMethod = bukkitImplAdapter.getClass().getMethod("getRelighterFactory");
                    Object relighterFactory = relighterFactoryMethod.invoke(bukkitImplAdapter);
                    Utils.log("&aFound FAWE's relighter factory: " + relighterFactory.toString());
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                Utils.log("&cTried preloading FAWE's relighter factory, but ran into an exception: " + e.getMessage());
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