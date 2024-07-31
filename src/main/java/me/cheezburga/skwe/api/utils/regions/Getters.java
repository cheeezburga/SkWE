package me.cheezburga.skwe.api.utils.regions;

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
        return new CuboidRegion(BukkitAdapter.adapt(world), Utils.toBlockVector3(loc1), Utils.toBlockVector3(loc2));
    }

    public static CylinderRegion getCylinderRegion(Location center, double radiusX, double radiusZ, int height) {
        return new CylinderRegion(BukkitAdapter.adapt(center.getWorld()), Utils.toBlockVector3(center), Vector2.at(radiusX, radiusZ), center.getBlockY(), center.getBlockY() + height);
    }

    public static EllipsoidRegion getEllipsoidRegion(Location center, double radiusX, double radiusY, double radiusZ) {
        return new EllipsoidRegion(BukkitAdapter.adapt(center.getWorld()), Utils.toBlockVector3(center), Vector3.at(radiusX, radiusY, radiusZ));
    }

    public static ConvexPolyhedralRegion getConvexPolyRegion(Location... vertices) {
        // TODO: figure out why the fuck this only sometimes works
        // TODO: it seems random, might be a worldedit/fawe thing that im just not understanding
        ConvexPolyhedralRegion region = new ConvexPolyhedralRegion(BukkitAdapter.adapt(vertices[0].getWorld()));
        for (Location loc : vertices) {
            region.addVertex(Utils.toBlockVector3(loc));
        }
        return region;
    }
}
