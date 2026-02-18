package me.cheezburga.skwe.api.utils.regions;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.ConvexPolyhedralRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.EllipsoidRegion;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class RegionUtils {

    public static final Map<Class<?>, String> REGION_TYPES = new HashMap<>();
    static {
        REGION_TYPES.put(CuboidRegion.class, "cuboid");
        REGION_TYPES.put(CylinderRegion.class, "cylinder");
        REGION_TYPES.put(EllipsoidRegion.class, "ellipsoid");
        REGION_TYPES.put(ConvexPolyhedralRegion.class, "convex polyhedral");
    }

    /*
    A method to get the appropriate BlockVector3's given a horizontal and vertical parameter.
    Used when insetting or outsetting a region.

    This method was copied from WorldEdit's SelectionCommands class.
     */
    public static BlockVector3[] getChangesForEachDir(int amount, boolean onlyHorizontal, boolean onlyVertical) {
        Stream.Builder<BlockVector3> changes = Stream.builder();

        if (!onlyHorizontal) {
            changes.add(BlockVector3.UNIT_Y);
            changes.add(BlockVector3.UNIT_MINUS_Y);
        }

        if (!onlyVertical) {
            changes.add(BlockVector3.UNIT_X);
            changes.add(BlockVector3.UNIT_MINUS_X);
            changes.add(BlockVector3.UNIT_Z);
            changes.add(BlockVector3.UNIT_MINUS_Z);
        }

        return changes.build().map(v -> v.multiply(amount)).toArray(BlockVector3[]::new);
    }
}
