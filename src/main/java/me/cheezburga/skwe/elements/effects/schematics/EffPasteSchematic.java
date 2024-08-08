package me.cheezburga.skwe.elements.effects.schematics;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.schematics.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Schematic - Paste")
@Description("Pastes a schematic at a location(s). Can be rotated, and can choose whether it should ignore air or not.")
@Examples("paste schematic \"example_schematic\" at {locations::*} rotated by 90 while ignoring air")
@Since("1.1.0")
@RequiredPlugins("WorldEdit")
public class EffPasteSchematic extends SkWEEffect {

    static {
        Skript.registerEffect(EffPasteSchematic.class, "paste [schem[atic]] [named|with name] %string% at %locations% [(with rotation|rotated by) %-number% [degrees]] [air:[and] while ignoring air]");
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
            RunnableUtils.run(Runnables.getPasteRunnable(name, loc, rotation, null, ignoreAir, false, false));
        }
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public String toString(@Nullable Event event, boolean debug) {
        return "paste schematic using " + this.name.toString(event, debug) + " at " + this.locations.toString(event, debug) + " with rotation " + (this.rotation == null ? "0" : this.rotation.toString(event, debug)) + (ignoreAir ? " while ignoring air" : "");
    }
}
