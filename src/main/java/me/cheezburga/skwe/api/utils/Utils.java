package me.cheezburga.skwe.api.utils;

import ch.njol.skript.aliases.ItemType;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.jetbrains.annotations.Nullable;

public class Utils {

    @SuppressWarnings("deprecation")
    public static final String pluginPrefix = net.md_5.bungee.api.ChatColor.of("#00FFFF") + "[SkWE]" + ChatColor.RESET;
    public static final String patternPrefix = "(use (world[ ]edit|we) to|world[ ]edit|we)";

    public static BlockVector3 bv3From(Location location) {
        return BlockVector3.at(location.getX(), location.getY(), location.getZ());
    }

    public static Location locationFrom(BlockVector3 vector) {
        return new Location(null, vector.getX(), vector.getY(), vector.getZ());
    }

    public static Location locationFrom(BlockVector3 vector, Player player) {
        return locationFrom(vector, player.getWorld());
    }

    public static Location locationFrom(BlockVector3 vector, World world) {
        return new Location(world, vector.getX(), vector.getY(), vector.getZ());
    }

    @Nullable
    public static Pattern patternFrom(Object o) {
        ParserContext context = new ParserContext();
        context.setActor(BukkitAdapter.adapt(Bukkit.getConsoleSender()));
        if (o instanceof String string) {
            return WorldEdit.getInstance().getPatternFactory().parseFromInput(string, context);
        } else if (o instanceof ItemType item) {
            if (item.getItemMeta() instanceof BlockDataMeta meta) {
                Material material = item.getMaterial();
                if (material.isBlock())
                    return WorldEdit.getInstance().getPatternFactory().parseFromInput(meta.getBlockData(material).getAsString(), context);
            }
        } else if (o instanceof Pattern pattern) {
            return pattern;
        }
        return null;
    }

    /**
     * Create a WorldEdit Mask from an input object
     *
     * @param o         input object
     * @param context   nullable ParserContext
     * @return          WorldEdit Mask
     */
    @Nullable
    public static Mask maskFrom(Object o, @Nullable ParserContext context) {
        if (context == null)
            context = new ParserContext();

        if (context.getActor() == null)
            context.setActor(BukkitAdapter.adapt(Bukkit.getConsoleSender()));

        if (o instanceof String string) {
            return WorldEdit.getInstance().getMaskFactory().parseFromInput(string, context);
        } else if (o instanceof ItemType item) {
            if (item.getItemMeta() instanceof BlockDataMeta meta) {
                Material material = item.getMaterial();
                if (material.isBlock())
                    return WorldEdit.getInstance().getMaskFactory().parseFromInput(meta.getBlockData(material).getAsString(), context);
            }
        } else if (o instanceof Mask mask) {
            return mask;
        }
        return null;
    }

    public static void updatePlayerPosition(Player player, Location loc, int position) {

    }
}
