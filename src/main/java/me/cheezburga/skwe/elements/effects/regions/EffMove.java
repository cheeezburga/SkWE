package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.util.Direction;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffMove extends Effect {

    static {
        Skript.registerEffect(EffMove.class, "move [mask:blocks that match %-itemtype/string% in] %worldeditregion% (0:up|1:down|2:north|3:south|4:east|5:west) [%-number% (times|blocks)] [and fill the area with %-itemtype/string%] [air:while ignoring air] [entities:while copying entities] [biomes:while copying biomes]");
    }

    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int NORTH = 2;
    private static final int SOUTH = 3;
    private static final int EAST = 4;
    private static final int WEST = 5;

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
        distance = (Expression<Number>) exprs[3];
        prePattern = exprs[4];
        ignoreAir = parseResult.hasTag("air");
        copyEntities = parseResult.hasTag("entities");
        copyBiomes = parseResult.hasTag("biomes");
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        Object preMask = this.preMask.getSingle(event);
        Pattern pattern = Utils.patternFrom(this.prePattern.getSingle(event));
        Number distance = this.distance.getSingle(event);
        if (distance == null)
            distance = 1;

        RunnableUtils.run(Runnables.getMoveRunnable(wrapper, pattern, preMask, getDirection(this.direction).toBlockVector(), (int) distance, ignoreAir, copyEntities, copyBiomes));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        if (preMask != null)
            return "move blocks in " + wrapper.toString(event, debug) + " that match " + preMask.toString(event, debug) + (distance != null ? distance.toString(event, debug) + " blocks" : "") + getDirection(direction).toString() + " and fill the area with " + (prePattern != null ? prePattern.toString(event, debug) : "air") + (ignoreAir ? " while ignoring air" : "") + (copyEntities ? " while copying entities" : "") + (copyBiomes ? " while copying biomes" : "");
        else
            return "move " + wrapper.toString(event, debug) + (distance != null ? distance.toString(event, debug) + " blocks" : "") + getDirection(direction).toString() + " and fill the area with " + (prePattern != null ? prePattern.toString(event, debug) : "air") + (ignoreAir ? " while ignoring air" : "") + (copyEntities ? " while copying entities" : "") + (copyBiomes ? " while copying biomes" : "");
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
