package me.cheezburga.skwe.api.utils;

import me.cheezburga.skwe.SkWE;
import org.bukkit.Bukkit;

public class RunnableUtils {

    public static void run(Runnable runnable) {
        run(runnable, true);
    }

    public static void run(Runnable runnable, boolean blocking) {
        if (blocking) {
            runnable.run();
        } else {
            if (SkWE.HAS_FAWE) {
                Bukkit.getScheduler().runTaskAsynchronously(SkWE.getInstance(), runnable);
            } else {
                Bukkit.getScheduler().runTask(SkWE.getInstance(), runnable);
            }
        }
    }

}
