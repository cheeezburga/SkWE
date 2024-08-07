package me.cheezburga.skwe.api.utils;

import ch.njol.skript.aliases.ItemType;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.internal.registry.AbstractFactory;
import com.sk89q.worldedit.math.BlockVector3;
import me.cheezburga.skwe.SkWE;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class Utils {

    @SuppressWarnings("deprecation")
    public static final String PLUGIN_PREFIX = ChatColor.of("#00FFFF") + "[skript-worldedit]" + ChatColor.RESET;
    public static final String PATTERN_PREFIX = "(use ([fastasync]world[ ]edit|[fa]we) to|[fastasync]world[ ]edit|[fa]we)";
    private static final java.util.regex.Pattern HEX_PATTERN = java.util.regex.Pattern.compile("<#([A-Fa-f\\d]){6}>");
    private static final boolean SKRIPT_EXISTS = Bukkit.getPluginManager().getPlugin("Skript") != null;

    public static final String MASK_TYPES = "%string/itemtype/worldeditmask%";
    public static final String MASK_TYPES_OPTIONAL = "%-string/itemtype/worldeditmask%";
    public static final String PATTERN_TYPES = "%string/itemtype/worldeditpattern%";
    public static final String PATTERN_TYPES_OPTIONAL = "%-string/itemtype/worldeditpattern%";
    public static final String LAZILY = " [:lazily]";

    public static final Pattern AIR_PATTERN = patternFrom("air");

    /**
     * Takes a Bukkit Location and returns the respective WorldEdit BlockVector3
     *
     * @param location  input Location
     * @return          WorldEdit BlockVector3
     */
    public static BlockVector3 toBlockVector3(Location location) {
        return BlockVector3.at(location.getX(), location.getY(), location.getZ());
    }

    public static List<BlockVector3> toBlockVector3List(List<Location> locations) {
        return locations.stream().map(Utils::toBlockVector3).collect(Collectors.toList());
    }

    public static BlockVector3[] toBlockVector3Array(Location[] locations) {
        return Arrays.stream(locations).map(Utils::toBlockVector3).toArray(BlockVector3[]::new);
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
            } else if (o instanceof PatternWrapper wrapper) {
                return wrapper.pattern();
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
                return SkWE.HAS_FAWE ? ((AbstractFactory<Mask>) WorldEdit.getInstance().getMaskFactory()).parseFromInput(string, context) :
                        WorldEdit.getInstance().getMaskFactory().parseFromInput(string, context);
            } else if (o instanceof ItemType item) {
                if (item.getItemMeta() instanceof BlockDataMeta meta) {
                    Material material = item.getMaterial();
                    if (material.isBlock())
                        return SkWE.HAS_FAWE ? ((AbstractFactory<Mask>) WorldEdit.getInstance().getMaskFactory()).parseFromInput(meta.getBlockData(material).getAsString(), context) :
                                WorldEdit.getInstance().getMaskFactory().parseFromInput(meta.getBlockData(material).getAsString(), context);
                }
            } else if (o instanceof MaskWrapper wrapper) {
                return maskFrom(wrapper.asString(), context);
            } else if (o instanceof Mask mask) {
                return mask;
            }
        } catch (InputParseException ignored) {}
        return null;
    }

    @Nullable
    public static String templateFrom(Object... input) {
        if (input.length == 0)
            return null;
        StringBuilder output = new StringBuilder();
        for (Object o : input) {
            if (o instanceof String string) {
                output.append(string); // TODO: should this check if its a valid string first? maybe try parsing and checking result?
            } else if (o instanceof ItemType item) {
                if (item.getItemMeta() instanceof BlockDataMeta meta) {
                    Material material = item.getMaterial();
                    if (material.isBlock())
                        output.append(meta.getBlockData(material).getAsString());
                }
            }
        }
        return output.toString().isEmpty() ? null : output.toString();
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

    /**
     * Method to get a coloured string from a string.
     * <p>
     *     This method is copied from SkBee.
     *     <a href="https://github.com/ShaneBeee/SkBee/blob/f6f85e3d9d9da0cd772b58e59fc288f7d1ec21f8/src/main/java/com/shanebeestudios/skbee/api/util/Util.java#L35">getColString(string)</a>
     * </p>
     *
     * @author ShaneBee
     *
     * @param string The string to convert to it's coloured version.
     * @return The coloured string.
     */
    @SuppressWarnings("deprecation") // Paper deprecation
    public static String getColouredString(String string) {
        Matcher matcher = HEX_PATTERN.matcher(string);
        if (SKRIPT_EXISTS) {
            while (matcher.find()) {
                ChatColor hexColor = ChatColor.of(matcher.group().substring(1, matcher.group().length() - 1));
                String before = string.substring(0, matcher.start());
                String after = string.substring(matcher.end());
                string = before + hexColor + after;
                matcher = HEX_PATTERN.matcher(string);
            }
        } else {
            string = HEX_PATTERN.matcher(string).replaceAll("");
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Method to log a coloured message to the console.
     * <p>
     *     This method is copied from SkBee.
     *     <a href="https://github.com/ShaneBeee/SkBee/blob/f6f85e3d9d9da0cd772b58e59fc288f7d1ec21f8/src/main/java/com/shanebeestudios/skbee/api/util/Util.java#L55">log(format, objects)</a>
     * </p>
     *
     * @author ShaneBee
     *
     * @param format The format for the log.
     * @param objects The arguments for the log.
     */
    public static void log(String format, Object... objects) {
        String log = String.format(format, objects);
        Bukkit.getConsoleSender().sendMessage(getColouredString(PLUGIN_PREFIX + " " + log));
    }
}
