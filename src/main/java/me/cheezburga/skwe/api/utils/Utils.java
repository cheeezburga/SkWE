package me.cheezburga.skwe.api.utils;

import ch.njol.skript.aliases.ItemType;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.jetbrains.annotations.Nullable;

public class Utils {

    @SuppressWarnings("deprecation")
    public static final String pluginPrefix = net.md_5.bungee.api.ChatColor.of("#00FFFF") + "[SkWE]" + ChatColor.RESET;
    public static final String patternPrefix = "(use (world[ ]edit|we) to|world[ ]edit|we)";

    /**
     * Takes a Bukkit Location and returns the respective WorldEdit BlockVector3
     *
     * @param location  input Location
     * @return          WorldEdit BlockVector3
     */
    public static BlockVector3 blockVector3From(Location location) {
        return BlockVector3.at(location.getX(), location.getY(), location.getZ());
    }

    /**
     * Takes a BlockVector3 and a nullable World and returns the respective Bukkit Location
     *
     * @param vector    input BlockVector3
     * @param world     nullable World
     * @return Bukkit   Location
     */
    public static Location locationFrom(BlockVector3 vector, @Nullable World world) {
        return new Location(world, vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Creates a WorldEdit Pattern from an input object
     *
     * @param o     input object
     * @return      WorldEdit Pattern
     */
    @Nullable
    public static Pattern patternFrom(Object o) {
        ParserContext context = new ParserContext();
        context.setActor(BukkitAdapter.adapt(Bukkit.getConsoleSender()));

        try {
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
        } catch (InputParseException e) {
            return null;
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

        try {
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
        } catch (InputParseException e) { return null; }
        return null;
    }

    /**
     * Create a WorldEdit ParserContext from an Extent and World
     *
     * @param extent    Extent object
     * @param world     World object
     * @return          WorldEdit ParserContext
     */
    public static ParserContext contextFrom(Extent extent, World world) {
        ParserContext context = new ParserContext();
        context.setExtent(extent);
        context.setWorld(BukkitAdapter.adapt(world));
        return context;
    }
}
