package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.util.Direction;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Expand/Contract")
@Description({
    "Makes a region larger or smaller in a given direction by a given amount. Optionally takes an opposite amount to expand the region in the other direction.",
    "Regions can also be expanded vertically, which makes them span from the min to the max of the world they're in."
})
@Examples("expand {region} up by 10 blocks and 3 in opposite direction")
@Since("1.0.4")
@RequiredPlugins("WorldEdit")
public class EffExpandContract extends SkWEEffect {

    static {
        Skript.registerEffect(EffExpandContract.class,
            "(:expand|contract) %worldeditregions% (0:up|1:down|2:north|3:south|4:east|5:west) [[by] %-number% [blocks|times]] [and %-number% [in opposite direction]]",
            "expand %worldeditregions% vertically");
    }

    private static final int UP = 0, DOWN = 1, NORTH = 2, SOUTH = 3, EAST = 4, WEST = 5;

    private Expression<RegionWrapper> wrappers;
    private Expression<Number> distance;
    private Expression<Number> reverseDistance;
    private int direction;
    private boolean expand;
    private boolean vert;

    @Override
    @SuppressWarnings({"unchecked"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrappers = (Expression<RegionWrapper>) exprs[0];
        distance = (Expression<Number>) exprs[1];
        reverseDistance = (Expression<Number>) exprs[2];
        direction = parseResult.mark;
        expand = parseResult.hasTag("expand");
        vert = matchedPattern == 1;
        return true;
    }

    @Override
    protected void execute(Event event) {
        if (vert) {
            for (RegionWrapper wrapper : wrappers.getArray(event)) {
                wrapper.expandVert();
            }
        } else {
            Direction direction = getDirection(this.direction); // should never be null
            int distance = (this.distance == null) ? 1 : this.distance.getOptionalSingle(event).orElse(1).intValue();
            int reverseDistance = (this.reverseDistance == null) ? 0 : this.reverseDistance.getOptionalSingle(event).orElse(0).intValue();

            for (RegionWrapper wrapper : wrappers.getArray(event)) {
                if (expand) {
                    wrapper.expand(direction, distance, reverseDistance);
                } else {
                    wrapper.contract(direction, distance, reverseDistance);
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return new SyntaxStringBuilder(event, debug)
            .append("expand ", wrappers, " by ")
            .append(distance == null ? "1" : distance)
            .append(reverseDistance == null ? "0" : reverseDistance)
            .toString();
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
