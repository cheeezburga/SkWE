package me.cheezburga.skwe.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.ContextlessEvent;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.shape.EntryValidators;
import me.cheezburga.skwe.api.utils.shape.Runnables;
import me.cheezburga.skwe.elements.types.WorldEditShape;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator.EntryValidatorBuilder;

import java.util.List;

public class SecCreateShape extends Section {

    static {
        Skript.registerSection(SecCreateShape.class, "create [a] %*worldeditshape% at %locations%");
    }

    private WorldEditShape shape;
    private Expression<Location> locs;

    private Expression<Pattern> pattern;
    private Expression<Boolean> hollow;
    private Expression<Number> radius, radiusY, radiusZ, height, thickness;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
        shape = ((Expression<WorldEditShape>) exprs[0]).getSingle(ContextlessEvent.get());
        if (shape == null) return false; // needed? will skript throw an error before it reaches here if it isnt valid?
        EntryValidatorBuilder builder = EntryValidators.get(shape);
        EntryContainer container = builder.build().validate(sectionNode);
        if (container == null) return false;

        pattern = (Expression<Pattern>) container.getOptional("pattern", false);
        if (pattern == null) return false;
        hollow = (Expression<Boolean>) container.getOptional("hollow", true);

        switch (shape) {
            case CYLINDER:
                height = (Expression<Number>) container.getOptional("height", false);
                thickness = (Expression<Number>) container.getOptional("thickness", true);
            case CIRCLE:
            case SPHERE:
                radius = (Expression<Number>) container.getOptional("radius", false);
                radiusZ = (Expression<Number>) container.getOptional("radiusZ", false);
                if (shape != WorldEditShape.CYLINDER)
                    radiusY = (Expression<Number>) container.getOptional("radiusY", false);
                break;
            case PYRAMID:
                radius = (Expression<Number>) container.getOptional("size", false);
        }
        if (radius == null || height == null) return false;
        if (radiusY == null && (shape == WorldEditShape.CIRCLE || shape == WorldEditShape.SPHERE)) radiusY = radius;
        if (radiusZ == null && (shape != WorldEditShape.PYRAMID)) radiusZ = radius;

        locs = (Expression<Location>) exprs[1];

        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        execute(event);
        return super.walk(event, false);
    }

    private void execute(Event event) {
        Pattern pattern = this.pattern.getSingle(event);
        if (pattern == null) return;

        Number radius = null, radiusY = null, radiusZ = null, height = null, thickness = null;
        double rX, rY, rZ, h, t;

        switch (shape) {
            case CYLINDER:
                height = this.height.getSingle(event);
                thickness = this.thickness.getSingle(event);
            case SPHERE:
            case CIRCLE:
            case PYRAMID:
                radius = this.radius.getSingle(event);
                if (shape != WorldEditShape.PYRAMID) {
                    radiusZ = this.radiusZ.getSingle(event);
                    if (shape != WorldEditShape.CYLINDER)
                        radiusY = this.radiusY.getSingle(event);
                }
                break;
        }

        if (radius == null) return;
        rX = radius.doubleValue();
        rY = (radiusY == null) ? rX : radiusY.doubleValue();
        rZ = (radiusZ == null) ? rX : radiusZ.doubleValue();
        t = (thickness == null) ? 0 : thickness.doubleValue();
        h = (height == null) ? 5 : height.intValue();

        boolean hollow = Boolean.FALSE.equals(this.hollow.getSingle(event));

        for (Location loc : locs.getArray(event)) {
            Runnable runnable = null;
            switch (shape) {
                case CIRCLE, SPHERE -> runnable = Runnables.getSphereRunnable(loc, pattern, hollow, rX, rY, rZ);
                case CYLINDER -> runnable = Runnables.getCylinderRunnable(loc, pattern, hollow, rX, rZ, (int) h, t);
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
