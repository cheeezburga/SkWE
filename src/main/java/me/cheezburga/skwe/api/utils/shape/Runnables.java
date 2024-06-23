package me.cheezburga.skwe.api.utils.shape;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.SkWE;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.Location;

public class Runnables {

    public static Runnable getSphereRunnable(Location loc, Pattern pattern, boolean hollow, double radius, double radiusY, double radiusZ) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
                session.makeSphere(Utils.blockVector3From(loc), pattern, radius, radiusY, radiusZ, !hollow);
                SkWE.getInstance().getLocalSession().remember(session);
            }
        };
    }

    public static Runnable getCylinderRunnable(Location loc, Pattern pattern, boolean hollow, double radius, double radiusZ, int height, double thickness) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
                session.makeCylinder(Utils.blockVector3From(loc), pattern, radius, radiusZ, height, thickness, !hollow);
                SkWE.getInstance().getLocalSession().remember(session);
            }
        };
    }

    public static Runnable getPyramidRunnable(Location loc, Pattern pattern, boolean hollow, int size) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
                session.makePyramid(Utils.blockVector3From(loc), pattern, size, !hollow);
                SkWE.getInstance().getLocalSession().remember(session);
            }
        };
    }

}
