package me.cheezburga.skwe.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.ContextlessEvent;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.shape.EntryValidators;
import me.cheezburga.skwe.api.utils.shape.Runnables;
import me.cheezburga.skwe.elements.types.WorldEditShape;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator.EntryValidatorBuilder;

import java.util.List;

@Name("Shape - General")
@Description({
    "Creates a shape at a given location. Depending on which shape is being created, a different set of entries can be used.",
    "",
    "Entries that can be used with any shape:",
    "* pattern (pattern): the pattern that the shape will be made out of (no default, and this has to be a pattern object (via the pattern expression))",
    "* hollow (boolean): whether the shape will be hollow (default is true)",
    "",
    "Entries that can be used with a pyramid:",
    "* size (integer): the size of the pyramid (no default)",
    "",
    "Entries that can be used with both a sphere and cylinder:",
    "* radius (number): the radius (on the x-axis) of the shape (no default)",
    "* radiusZ (number): the radius (on the z-axis) of the shape (this is optional, and if not provided, will default to the radius)",
    "",
    "Entries that can be used only with a sphere:",
    "* radiusY (number): the radius (on the y-axis) of the shape (this is optional, and if not provided, will default to the radius)",
    "",
    "Entries that can be used only with a cylinder:",
    "* height (number): the height of the cylinder (no default)",
    "",
    "This section is effectively the same as just using the shape effects, but might be preferable."
})
@Examples({
    "create a sphere at {location}:",
        "\tpattern: pattern of stone",
        "\thollow: false",
        "\tradius: 5",
        "\tradiusY: 4",
        "\tradiusZ: 3"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class SecCreateShape extends Section {

    static {
        Skript.registerSection(SecCreateShape.class, "create [a] %*worldeditshape% at %locations%");
    }

    private WorldEditShape shape;
    private Expression<Location> locations;

    private Expression<Pattern> pattern;
    private Expression<Boolean> hollow;
    private Expression<Number> radius, radiusY, radiusZ, height;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
        shape = ((Expression<WorldEditShape>) exprs[0]).getSingle(ContextlessEvent.get());
        if (shape == null) return false; // needed? will skript throw an error before it reaches here if it isn't valid?
        EntryValidatorBuilder builder = EntryValidators.get(shape);
        EntryContainer container = builder.build().validate(sectionNode);
        if (container == null) return false;

        pattern = (Expression<Pattern>) container.get("pattern", false);
        hollow = (Expression<Boolean>) container.getOptional("hollow", true);

        switch (shape) {
            case CYLINDER:
                height = (Expression<Number>) container.getOptional("height", false);
            case ELLIPSE:
            case ELLIPSOID:
            case SPHERE:
                radius = (Expression<Number>) container.getOptional("radius", false);
                radiusZ = (Expression<Number>) container.getOptional("radiusZ", false);
                if (shape != WorldEditShape.CYLINDER)
                    radiusY = (Expression<Number>) container.getOptional("radiusY", false);
                break;
            case PYRAMID:
                radius = (Expression<Number>) container.getOptional("size", false);
        }
        if (radiusY == null && (shape == WorldEditShape.ELLIPSE || shape == WorldEditShape.ELLIPSOID || shape == WorldEditShape.SPHERE)) radiusY = radius;
        if (radiusZ == null && (shape != WorldEditShape.PYRAMID)) radiusZ = radius;

        locations = (Expression<Location>) exprs[1];

        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        execute(event);
        return super.walk(event, false);
    }

    private void execute(Event event) {
        Object prePattern = this.pattern.getSingle(event);
        Pattern pattern = Utils.patternFrom(prePattern);
        if (pattern == null) {
            error("The provided pattern was not set!");
            return;
        }

        Location[] locations = this.locations.getArray(event);
        if (locations.length < 1) {
            warning("No location(s) was provided!", Utils.toHighlight(this.locations));
            return;
        }

        Number radius = null, radiusY = null, radiusZ = null, height = null;
        double rX, rY, rZ;
        int h;

        switch (shape) {
            case CYLINDER:
                height = this.height.getSingle(event);
            case ELLIPSE:
            case ELLIPSOID:
            case SPHERE:
            case PYRAMID:
                radius = this.radius.getSingle(event);
                if (shape != WorldEditShape.PYRAMID) {
                    radiusZ = this.radiusZ.getSingle(event);
                    if (shape != WorldEditShape.CYLINDER)
                        radiusY = this.radiusY.getSingle(event);
                }
                break;
        }

        if (radius == null) {
            error("The provided radius was not set!");
            return;
        }
        rX = radius.doubleValue();
        rY = (radiusY == null) ? rX : radiusY.doubleValue();
        rZ = (radiusZ == null) ? rX : radiusZ.doubleValue();
        h = (height == null) ? 5 : height.intValue();

        boolean hollow = Boolean.TRUE.equals(this.hollow.getSingle(event));

        for (Location loc : locations) {
            Runnable runnable = null;
            switch (shape) {
                case ELLIPSOID, ELLIPSE, SPHERE -> runnable = Runnables.getSphereRunnable(loc, pattern, hollow, rX, rY, rZ);
                case CYLINDER -> runnable = Runnables.getCylinderRunnable(loc, pattern, hollow, rX, rZ, h);
                case PYRAMID -> runnable = Runnables.getPyramidRunnable(loc, pattern, hollow, (int) rX);
            }

            if (runnable != null)
                RunnableUtils.run(runnable);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create worldedit shape";
    }

}
