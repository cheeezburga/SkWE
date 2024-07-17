package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.util.Direction;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Move")
@Description({
    "Moves the blocks in a given region in a given direction a given distance.",
    "You can choose to only move the blocks that match a given mask too, as well as filling the left-behind area using a certain pattern.",
    "Additionally, air can be ignored, and entities and biomes can be copied.",
    "",
    "By default, the distance will be 1, all blocks will be moved, no pattern will be left behind, air will not be ignored, and entities and biomes will not be copied."
})
@Examples({
    "move {region} up 10 blocks and fill the area with glass block while ignoring air while copying entities while copying biomes"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffMove extends SkWEEffect {

    static {
        Skript.registerEffect(EffMove.class, "move [mask:blocks that match " + Utils.MASK_TYPES_OPTIONAL + " in] %worldeditregion% (0:up|1:down|2:north|3:south|4:east|5:west) [%-number% (time|block)[s]] [and fill the area with " + Utils.PATTERN_TYPES_OPTIONAL + "] [air:while ignoring air] [entities:while copying entities] [biomes:while copying biomes]" + Utils.LAZILY);
    }

    private static final int UP = 0, DOWN = 1, NORTH = 2, SOUTH = 3, EAST = 4, WEST = 5;

    private Expression<?> preMask;
    private Expression<RegionWrapper> wrapper;
    private int direction;
    private Expression<Number> distance;
    private Expression<?> prePattern;
    private boolean ignoreAir, copyEntities, copyBiomes;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        preMask = exprs[0];
        wrapper = (Expression<RegionWrapper>) exprs[1];
        direction = parseResult.mark;
        distance = (Expression<Number>) exprs[2];
        prePattern = exprs[3];
        ignoreAir = parseResult.hasTag("air");
        copyEntities = parseResult.hasTag("entities");
        copyBiomes = parseResult.hasTag("biomes");
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        Object preMask = null;
        Pattern pattern = null;
        if (this.preMask != null) {
            preMask = this.preMask.getSingle(event);
            if (preMask == null)
                return;
        }
        if (this.prePattern != null) {
            pattern = Utils.patternFrom(this.prePattern.getSingle(event));
            if (pattern == null)
                return;
        }

        int distance = (this.distance == null) ? 1 : this.distance.getOptionalSingle(event).orElse(1).intValue();

        RunnableUtils.run(Runnables.getMoveRunnable(wrapper, pattern, preMask, getDirection(this.direction).toBlockVector(), distance, ignoreAir, copyEntities, copyBiomes), isBlocking());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        if (preMask != null)
            return "move blocks in " + wrapper.toString(event, debug) + " that match " + preMask.toString(event, debug) + (distance != null ? distance.toString(event, debug) + " blocks" : "") + getDirection(direction).toString() + " and fill the area with " + (prePattern != null ? prePattern.toString(event, debug) : "air") + (ignoreAir ? " while ignoring air" : "") + (copyEntities ? " while copying entities" : "") + (copyBiomes ? " while copying biomes" : "") + (isBlocking() ? "" : " lazily");
        else
            return "move " + wrapper.toString(event, debug) + (distance != null ? distance.toString(event, debug) + " blocks" : "") + getDirection(direction).toString() + " and fill the area with " + (prePattern != null ? prePattern.toString(event, debug) : "air") + (ignoreAir ? " while ignoring air" : "") + (copyEntities ? " while copying entities" : "") + (copyBiomes ? " while copying biomes" : "") + (isBlocking() ? "" : " lazily");
    }

    private Direction getDirection(int mark) {
        return switch (mark) {
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case EAST -> Direction.EAST;
            case WEST -> Direction.WEST;
            default -> null;
        };
    }
}
