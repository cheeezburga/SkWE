package me.cheezburga.skwe.api.utils.regions;

import com.sk89q.worldedit.regions.ConvexPolyhedralRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.World;

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
