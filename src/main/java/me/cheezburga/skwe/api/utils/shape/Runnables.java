package me.cheezburga.skwe.api.utils.shape;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.Location;

public class Runnables {

    // should be fine to ignore the MaxChangedBlocksException; it should default to -1 when created via the Instance#newEditSession method

    public static Runnable getSphereRunnable(Location loc, Pattern pattern, boolean hollow, double radius, double radiusY, double radiusZ) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
                try {
                    session.makeSphere(Utils.blockVector3From(loc), pattern, radius, radiusY, radiusZ, !hollow);
                } catch (MaxChangedBlocksException ignored) {}
            }
        };
    }

    public static Runnable getCylinderRunnable(Location loc, Pattern pattern, boolean hollow, double radius, double radiusZ, int height) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
                try {
                    session.makeCylinder(Utils.blockVector3From(loc), pattern, radius, radiusZ, height, !hollow);
                } catch (MaxChangedBlocksException ignored) {}
            }
        };
    }

    public static Runnable getPyramidRunnable(Location loc, Pattern pattern, boolean hollow, int size) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
                try {
                    session.makePyramid(Utils.blockVector3From(loc), pattern, size, !hollow);
                } catch (MaxChangedBlocksException ignored) {}
            }
        };
    }

}
