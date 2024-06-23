package me.cheezburga.skwe.api.utils.regions;

import com.fastasyncworldedit.core.regions.PolyhedralRegion;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.Vector2;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.ConvexPolyhedralRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;

public class Getters {

    public static CuboidRegion getCuboidRegion(Location loc1, Location loc2, World world) {
        return new CuboidRegion(BukkitAdapter.adapt(world), Utils.blockVector3From(loc1), Utils.blockVector3From(loc2));
    }

    public static CylinderRegion getCylinderRegion(Location center, double radiusX, double radiusZ, int height) {
        return new CylinderRegion(BukkitAdapter.adapt(center.getWorld()), Utils.blockVector3From(center), Vector2.at(radiusX, radiusZ), center.getBlockY(), center.getBlockY() + height);
    }

    public static EllipsoidRegion getEllipsoidRegion(Location center, double radiusX, double radiusY, double radiusZ) {
        return new EllipsoidRegion(BukkitAdapter.adapt(center.getWorld()), Utils.blockVector3From(center), Vector3.at(radiusX, radiusY, radiusZ));
    }

    public static PolyhedralRegion getPolyRegion(Location... vertices) {
        PolyhedralRegion region = new PolyhedralRegion(BukkitAdapter.adapt(vertices[0].getWorld()));
        for (Location loc : vertices) {
            region.addVertex(Utils.blockVector3From(loc));
        }
        return region;
    }

    public static ConvexPolyhedralRegion getConvexPolyRegion(Location... vertices) {
        ConvexPolyhedralRegion region = new ConvexPolyhedralRegion(BukkitAdapter.adapt(vertices[0].getWorld()));
        for (Location loc : vertices) {
            region.addVertex(Utils.blockVector3From(loc));
        }
        return region;
    }
}
