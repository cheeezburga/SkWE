package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffInsetOutset extends SkWEEffect {

    static {
        Skript.registerEffect(EffInsetOutset.class,
                "(:in|out)set %worldeditregions% by %number% [blocks] [1:vertically|2:horizontally]");
    }

    private Expression<RegionWrapper> wrappers;
    private Expression<Number> distance;
    private boolean in;
    private int direction;

    @Override
    @SuppressWarnings({"unchecked", "NullableProblems"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrappers = (Expression<RegionWrapper>) exprs[0];
        distance = (Expression<Number>) exprs[1];
        in = parseResult.hasTag("in");
        direction = parseResult.mark;
        return true;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected void execute(Event event) {
        int distance = (this.distance == null) ? 1 : this.distance.getOptionalSingle(event).orElse(1).intValue();

        for (RegionWrapper wrapper : wrappers.getArray(event)) {
            if (in) {
                wrapper.inset(distance, this.direction);
            } else {
                wrapper.outset(distance, this.direction);
            }
        }
    }

    @Override
    @SuppressWarnings({"ConstantConditions", "NullableProblems"})
    public String toString(@Nullable Event event, boolean debug) {
        if (this.direction == 0) {
            return (in ? "in" : "out") + "set " + wrappers.toString(event, debug) + " by " + (distance != null ? distance.toString(event, debug) : "1");
        } else {
            String direction;
            if (this.direction == 1) {
                direction = "vertical";
            } else {
                direction = "horizontal";
            }
            return (in ? "in" : "out") + "set " + wrappers.toString(event, debug) + " by " + (distance != null ? distance.toString(event, debug) : "1") + " in only the " + direction + " direction";
        }
    }
}
