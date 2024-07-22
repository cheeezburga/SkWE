package me.cheezburga.skwe.elements.effects.shapes;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.shape.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Shape - Sphere")
@Description({
    "Creates a sphere using a given pattern at a certain location.",
    "You can choose for the sphere to be hollow, and it can take a set of radii.",
    "By default, the radius is 5, and the sphere will not be hollow."
})
@Examples({
    "create a hollow sphere made out of emerald block with radii (5,4,3) at {location}"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffCreateSphere extends SkWEEffect {

    static {
        Skript.registerEffect(EffCreateSphere.class,
                "create [a] [:hollow] (sphere|ellipsoid) ([made] out of|with|using) [pattern] " + Utils.PATTERN_TYPES + " [with radi(us|i) %-numbers%] at %locations%" + Utils.LAZILY);
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
        setBlocking(!parseResult.hasTag("lazily"));
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
            RunnableUtils.run(Runnables.getSphereRunnable(loc, pattern, hollow, rX, rY, rZ), isBlocking());
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create a " + (hollow ? "hollow " : "") + "sphere with radius " + radius.toString(event, debug) + " at locations " + locations.toString(event, debug) + (isBlocking() ? "" : " lazily");
    }
}
