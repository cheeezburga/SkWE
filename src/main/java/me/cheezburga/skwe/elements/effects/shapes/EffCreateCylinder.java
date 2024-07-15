package me.cheezburga.skwe.elements.effects.shapes;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.shape.Runnables;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Shape - Cylinder")
@Description({
    "Creates a cylinder using a given pattern at a certain location.",
    "You can choose for the cylinder to be hollow, and it can take a set of radii and a height.",
    "By default, the radius is 5, the height is 1, and the cylinder will not be hollow."
})
@Examples({
    "create a hollow cylinder made out of diamond block with radii (5,4) with height 10 at {location}"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffCreateCylinder extends Effect {

    static {
        Skript.registerEffect(EffCreateCylinder.class,
                "create [a] [:hollow] cylinder ([made] out of|with [pattern]) " + Utils.PATTERN_TYPES + " [with radi(us|i) %-numbers%] [with height %-number%] at %locations%");
    }

    private boolean hollow;
    private Expression<?> prePattern;
    private Expression<Number> radius, height;
    private Expression<Location> locations;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        hollow = parseResult.hasTag("hollow");
        prePattern = exprs[0];
        radius = (Expression<Number>) exprs[1];
        height = (Expression<Number>) exprs[2];
        locations = (Expression<Location>) exprs[3];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        Pattern pattern = Utils.patternFrom(this.prePattern.getSingle(event));
        if (pattern == null)
            return;

        double rX, rZ;
        if (this.radius == null) {
            rX = rZ = 5;
        } else {
            double[] radii = Arrays.stream(this.radius.getArray(event)).mapToDouble(Number::doubleValue).toArray();
            if (radii.length == 0) {
                rX = rZ = 5; // TODO: replace with config or make non-optional and fail
            } else if (radii.length == 1) {
                rX = rZ = radii[0];
            } else {
                rX = radii[0]; rZ = radii[1];
            }
        }

        int h = (this.height == null) ? 1 : height.getOptionalSingle(event).orElse(1).intValue(); //TODO: replace with config, or just fail

        for (Location loc : this.locations.getArray(event)) {
            RunnableUtils.run(Runnables.getCylinderRunnable(loc, pattern, hollow, rX, rZ, h));
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create a " + (hollow ? "hollow " : "") + "cylinder with radius " + radius.toString(event, debug) + " and height " + height.toString(event, debug) + " at locations " + locations.toString(event, debug);
    }
}
