package me.cheezburga.skwe.elements.effects.schematics;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.schematics.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffDeleteSchematic extends SkWEEffect {

    static {
        Skript.registerEffect(EffDeleteSchematic.class, "delete schem[atic][s] [named|with name] %strings%");
    }

    private Expression<String> names;

    @Override
    @SuppressWarnings({"unchecked", "NullableProblems"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        names = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        for (String name : names.getArray(event)) {
            RunnableUtils.run(Runnables.getDeleteRunnable(name));
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "delete schematic " + names.toString(event, debug);
    }

}
