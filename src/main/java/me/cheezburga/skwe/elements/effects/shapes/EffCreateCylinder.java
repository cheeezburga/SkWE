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
import me.cheezburga.skwe.lang.BlockingSyntaxStringBuilder;
import me.cheezburga.skwe.lang.SkWEEffect;
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
public class EffCreateCylinder extends SkWEEffect {

    static {
        Skript.registerEffect(EffCreateCylinder.class,
            "create [a] [:hollow] cylinder ([made] out of|with|using) [pattern] " + Utils.PATTERN_TYPES + " [with radi(us|i) %-numbers%][,| and] [with height %-number%] at %locations%" + Utils.LAZILY);
    }

    private boolean hollow;
    private Expression<?> prePattern;
    private Expression<Number> radius, height;
    private Expression<Location> locations;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        hollow = parseResult.hasTag("hollow");
        prePattern = exprs[0];
        radius = (Expression<Number>) exprs[1];
        height = (Expression<Number>) exprs[2];
        locations = (Expression<Location>) exprs[3];
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

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
            RunnableUtils.run(Runnables.getCylinderRunnable(loc, pattern, hollow, rX, rZ, h), isBlocking());
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return new BlockingSyntaxStringBuilder(event, debug, isBlocking())
            .append("create a ", (hollow ? "hollow" : ""), "cylinder with radius ", radius,
                    " and height ", height, " at locations ", locations)
            .toString();
    }

}
