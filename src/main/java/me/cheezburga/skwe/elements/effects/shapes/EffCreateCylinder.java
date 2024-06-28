package me.cheezburga.skwe.elements.effects.shapes;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.shape.Runnables;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffCreateCylinder extends Effect {

    static {
        Skript.registerEffect(EffCreateCylinder.class,
                "create [a] [:hollow] cylinder ([made] out of|with [pattern]) %itemtype/string% [with radius %-number%] [with height %-number%] at %locations%");
    }

    private boolean hollow;
    private Expression<?> prePattern;
    private Expression<Number> radius, height;
    private Expression<Location> locations;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        hollow = parseResult.hasTag("hollow");
        prePattern = exprs[0];
        radius = (Expression<Number>) exprs[1];
        height = (Expression<Number>) exprs[2];
        locations = (Expression<Location>) exprs[4];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        Pattern pattern = Utils.patternFrom(prePattern.getSingle(event));
        if (pattern == null)
            return;

        double r = radius.getOptionalSingle(event).orElse(5).doubleValue(); //TODO: replace with config, or just fail
        int h = height.getOptionalSingle(event).orElse(1).intValue(); //TODO: replace with config, or just fail

        for (Location loc : locations.getArray(event)) {
            RunnableUtils.run(Runnables.getCylinderRunnable(loc, pattern, hollow, r, r, h));
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create a " + (hollow ? "hollow " : "") + "cylinder with radius " + radius.toString(event, debug) + " and height " + height.toString(event, debug) + " at locations " + locations.toString(event, debug);
    }
}
