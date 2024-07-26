package me.cheezburga.skwe.api.utils.regions;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.regions.ConvexPolyhedralRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Countable;
import com.sk89q.worldedit.world.block.BlockState;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.Map;

public record RegionWrapper(Region region, World world) {

    private String getRegionType() {
        if (this.region instanceof CuboidRegion) {
            return "cuboid";
        } else if (this.region instanceof CylinderRegion) {
            return "cylinder";
        } else if (this.region instanceof EllipsoidRegion) {
            return "ellipsoid";
        } else if (this.region instanceof ConvexPolyhedralRegion) {
            return "convex polyhedral";
        }
        return "unknown";
    }

    public int countBlocks(Object preMask) {
        try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world()))) {
            Mask mask = Utils.maskFrom(preMask, Utils.contextFrom(session, world()));
            if (mask != null)
                return session.countBlocks(region(), mask);
        }
        return -1;
    }

    public Map<BlockData, Number> getDistribution(boolean separateStates) {
        Map<BlockData, Number> map = new HashMap<>();
        try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world()))) {
            for (Countable<BlockState> countable : session.getBlockDistribution(region(), separateStates)) {
                map.put(BukkitAdapter.adapt(countable.getID()), countable.getAmount());
            }
        }
        return map;
    }



    @Override
    public String toString() {
        // TODO: decide whether this should include coords of associated points - might get long for poly/convex
        return getRegionType() + " region in world " + this.world.getName();
    }

    public String toVariableString() {
        // TODO: change this to actually return a proper variable string
        return toString();
    }
}
