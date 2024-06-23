package me.cheezburga.skwe.api.utils.blocks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.Region;
import me.cheezburga.skwe.SkWE;
import org.bukkit.World;

public class Runnables {

    public static Runnable getSetRunnable(World world, Region region, Pattern pattern) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
                session.setBlocks(region, pattern);
                SkWE.getInstance().getLocalSession().remember(session);
            }
        };
    }

}
