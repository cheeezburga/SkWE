package me.cheezburga.skwe.api.utils.blocks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.Region;
import me.cheezburga.skwe.SkWE;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.World;

public class Runnables {

    public static Runnable getSetRunnable(World world, Region region, Pattern pattern) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
                try {
                    session.setBlocks(region, pattern);
                    SkWE.getInstance().getLocalSession().remember(session);
                } catch (MaxChangedBlocksException ignored) {}
            }
        };
    }

    public static Runnable getReplaceRunnable(World world, Region region, Pattern pattern, Object preMask) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
                Mask mask = Utils.maskFrom(preMask, Utils.contextFrom(session, world));
                if (mask == null) return;

                try {
                    session.replaceBlocks(region, mask, pattern);
                    SkWE.getInstance().getLocalSession().remember(session);
                } catch (MaxChangedBlocksException ignored) {}
            }
        };
    }

}
