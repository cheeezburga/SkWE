package me.cheezburga.skwe.api.utils.regions;

import com.fastasyncworldedit.core.regions.PolyhedralRegion;
import com.sk89q.worldedit.regions.ConvexPolyhedralRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.World;

public class RegionWrapper {

    final private Region region;
    final private World world;

    public RegionWrapper(Region region, World world) {
        this.region = region;
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    public Region getRegion() {
        return this.region;
    }

    private String getRegionType() {
        if (this.region instanceof CuboidRegion) {
            return "cuboid";
        } else if (this.region instanceof CylinderRegion) {
            return "cylinder";
        } else if (this.region instanceof EllipsoidRegion) {
            return "ellipsoid";
        } else if (this.region instanceof PolyhedralRegion) {
            return "polyhedral";
        } else if (this.region instanceof ConvexPolyhedralRegion) {
            return "convex polyhedral";
        }
        return "unknown";
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
