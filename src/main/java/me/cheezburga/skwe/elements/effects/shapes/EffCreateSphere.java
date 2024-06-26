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

import java.util.Arrays;

public class EffCreateSphere extends Effect {

    static {
        Skript.registerEffect(EffCreateSphere.class,
                "create [a] [:hollow] (sphere|ellipsoid) ([made] out of|with [pattern]) %itemtype/string% [with radi(us|i) %-numbers%] at %locations%");
    }

    private boolean hollow;
    private Expression<?> prePattern;
    private Expression<Number> radius;
    private Expression<Location> locations;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        hollow = parseResult.hasTag("hollow");
        prePattern = exprs[0];
        radius = (Expression<Number>) exprs[1];
        locations = (Expression<Location>) exprs[2];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        Pattern pattern = Utils.patternFrom(prePattern.getSingle(event));
        if (pattern == null)
            return;

        double rX, rY, rZ;
        double[] radii = Arrays.stream(radius.getArray(event)).mapToDouble(Number::doubleValue).toArray();
        if (radii.length == 0) {
            rX = rY = rZ = 5; // TODO: replace with config or make non-optional and fail
        } else if (radii.length == 1) {
            rX = rY = rZ = radii[0];
        } else if (radii.length == 2) {
            rX = rZ = radii[0]; rY = radii[1];
        } else {
            rX = radii[0]; rY = radii[1]; rZ = radii[2];
        }

        for (Location loc : locations.getArray(event)) {
            RunnableUtils.run(Runnables.getSphereRunnable(loc, pattern, hollow, rX, rY, rZ));
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create a " + (hollow ? "hollow " : "") + "sphere with radius " + radius.toString(event, debug) + " at locations " + locations.toString(event, debug);
    }
}
