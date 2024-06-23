package me.cheezburga.skwe.api.utils.regions;

import com.sk89q.worldedit.regions.Region;
import org.bukkit.World;

public class RegionWrapper {

    private Region region;
    private World world;

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

}
