package me.cheezburga.skwe.elements.effects.shapes;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.shape.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Shape - Pyramid")
@Description({
    "Creates a pyramid using a given pattern at a certain location.",
    "You can choose for the pyramid to be hollow, and it can take a size.",
    "By default, the size is 5, and the pyramid will not be hollow."
})
@Examples({
    "create a hollow pyramid made out of gold block with size 10 at {location}"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffCreatePyramid extends SkWEEffect {

    static {
        Skript.registerEffect(EffCreatePyramid.class,
                "create [a] [:hollow] pyramid ([made] out of|with|using) [pattern] " + Utils.PATTERN_TYPES + " [with size %-number%] at %locations%" + Utils.LAZILY);
    }

    private boolean hollow;
    private Expression<?> prePattern;
    private Expression<Number> size;
    private Expression<Location> locations;

    @SuppressWarnings({"NullableProblems","unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        hollow = parseResult.hasTag("hollow");
        prePattern = exprs[0];
        size = (Expression<Number>) exprs[1];
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

        int s = size.getOptionalSingle(event).orElse(5).intValue(); //TODO: replace with config, or just fail

        for (Location loc : locations.getArray(event)) {
            RunnableUtils.run(Runnables.getPyramidRunnable(loc, pattern, hollow, s), isBlocking());
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create a " + (hollow ? "hollow " : "") + "pyramid with size " + size.toString(event, debug) + " at locations " + locations.toString(event, debug) + (isBlocking() ? "" : " lazily");
    }
}
