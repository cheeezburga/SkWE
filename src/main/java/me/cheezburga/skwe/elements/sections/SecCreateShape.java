package me.cheezburga.skwe.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.ShapeEntryValidators;
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
        shape = ((Literal<WorldEditShape>) exprs[0]).getSingle();
        locs = (Expression<Location>) exprs[1];
        EntryValidatorBuilder builder = ShapeEntryValidators.get(shape);
        EntryContainer container = builder.build().validate(sectionNode);
        if (container == null) return false;

        pattern = (Expression<Pattern>) container.getOptional("pattern", false);
        if (pattern == null) return false;
        hollow = (Expression<Boolean>) container.getOptional("hollow", true);
        radius = shape != WorldEditShape.PYRAMID ?
                (Expression<Number>) container.getOptional("radius", false) :
                (Expression<Number>) container.getOptional("size", false);
        if (radius == null) return false;

        if (shape != WorldEditShape.PYRAMID) {
            radiusY = (Expression<Number>) container.getOptional("radiusY", false);
            radiusY = (Expression<Number>) container.getOptional("radiusZ", false);
            if (radiusY == null && (shape == WorldEditShape.SPHERE))
                radiusY = radius;
            if (radiusZ == null)
                radiusZ = radius;
        }

        if (shape == WorldEditShape.CYLINDER) {
            height = (Expression<Number>) container.getOptional("height", false);
            thickness = (Expression<Number>) container.getOptional("thickness", false);
            if (height == null || thickness == null) return false;
        }
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        return null;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return null;
    }
}
