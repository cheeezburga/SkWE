package me.cheezburga.skwe.api.utils.regions;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionOperationException;
import com.sk89q.worldedit.util.Countable;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.world.block.BlockState;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.Map;

import static me.cheezburga.skwe.api.utils.regions.Utils.REGION_TYPES;
import static me.cheezburga.skwe.api.utils.regions.Utils.getChangesForEachDir;

public record RegionWrapper(Region region, World world) {

    private String getRegionType() {
        return REGION_TYPES.getOrDefault(this.region.getClass(), "unknown");
    }

    public void inset(int distance, int direction) { // dir = 1:vertical/2:horizontal
        try {
            this.region.contract(getChangesForEachDir(distance, (direction == 2), (direction == 1)));
        } catch (RegionOperationException e) {
            Utils.log("&cTried insetting a region but ran into an exception: " + e.getMessage());
        }
    }

    public void outset(int distance, int direction) { // dir = 1:vertical/2:horizontal
        try {
            this.region.expand(getChangesForEachDir(distance, (direction == 2), (direction == 1)));
        } catch (RegionOperationException e) {
            Utils.log("&cTried outsetting a region but ran into an exception: " + e.getMessage());
        }
    }

    public void contract(Direction direction, int distance, int reverseDistance) {
        BlockVector3 vector = direction.toBlockVector();
        try {
            this.region.contract(vector.multiply(distance), vector.multiply(-reverseDistance));
        } catch (RegionOperationException e) {
            Utils.log("&cTried expanding a region but ran into an exception: " + e.getMessage());
        }
    }

    public void expand(Direction direction, int distance, int reverseDistance) {
        BlockVector3 vector = direction.toBlockVector();
        try {
            this.region.expand(vector.multiply(distance), vector.multiply(-reverseDistance));
        } catch (RegionOperationException e) {
            Utils.log("&cTried expanding a region but ran into an exception: " + e.getMessage());
        }
    }

    public void expandVert() {
        int height = BukkitAdapter.adapt(world).getMaxY() - BukkitAdapter.adapt(world).getMinY();
        try {
            this.region.expand(BlockVector3.at(0,height,0), BlockVector3.at(0,-height,0));
        } catch (RegionOperationException e) {
            Utils.log("&cTried expanding a region but ran into an exception: " + e.getMessage());
        }
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
        return getRegionType() + " region in world " + this.world.getName();
    }

    public String toVariableString() {
        // TODO: change this to actually return a proper variable string
        return toString();
    }
}
