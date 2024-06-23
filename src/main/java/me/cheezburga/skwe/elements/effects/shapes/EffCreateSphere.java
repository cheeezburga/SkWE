package me.cheezburga.skwe.elements.effects.shapes;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.shape.Runnables;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffCreateSphere extends Effect {

    static {
        Skript.registerEffect(EffCreateSphere.class,
                "create [a] [:hollow] (sphere|ellipsoid) ([made] out of|with [pattern]) %itemtype/string% [with radius %-number%] at %locations%");
    }

    private boolean hollow;
    private Expression<?> prePattern;
    private Expression<Number> radius;
    private Expression<Location> locations;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        hollow = parseResult.hasTag("hollow");
        prePattern = exprs[0];
        radius = (Expression<Number>) exprs[1];
        locations = (Expression<Location>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Pattern pattern = Utils.patternFrom(prePattern.getSingle(event));
        if (pattern == null)
            return;

        double r = radius.getOptionalSingle(event).orElse(5).doubleValue(); //TODO: replace with config, or just fail

        for (Location loc : locations.getArray(event)) {
            RunnableUtils.run(Runnables.getSphereRunnable(loc, pattern, hollow, r, r, r));
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create a " + (hollow ? "hollow " : "") + "sphere with radius " + radius.toString(event, debug) + " at locations " + locations.toString(event, debug);
    }
}
