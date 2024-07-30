package me.cheezburga.skwe.elements.effects.schematics;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.schematics.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffPasteSchematic extends SkWEEffect {

    static {
        Skript.registerEffect(EffPasteSchematic.class, "paste schem[atic] %string% at %locations% [(with rotation|rotated by) %-number%] [air:[and] while ignoring air]");
    }

    private Expression<String> name;
    private Expression<Location> locations;
    private Expression<Number> rotation;
    private boolean ignoreAir;

    @Override
    @SuppressWarnings({"unchecked","NullableProblems"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        locations = (Expression<Location>) exprs[1];
        rotation = (Expression<Number>) exprs[2];
        ignoreAir = parseResult.hasTag("air");
        return true;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected void execute(Event event) {
        String name = this.name.getSingle(event);
        if (name == null)
            return;

        int rotation = (this.rotation == null) ? 0 : this.rotation.getOptionalSingle(event).orElse(0).intValue();

        for (Location loc : locations.getArray(event)) {
            RunnableUtils.run(Runnables.getPasteRunnable(name, loc, rotation, ignoreAir));
        }
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public String toString(@Nullable Event event, boolean debug) {
        return "paste schematic named " + this.name.toString(event, debug) + " at " + this.locations.toString(event, debug) + " with rotation " + (this.rotation == null ? "0" : this.rotation.toString(event, debug)) + (ignoreAir ? " while ignoring air" : "");
    }
}
