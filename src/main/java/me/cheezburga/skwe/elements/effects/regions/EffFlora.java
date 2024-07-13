package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffFlora extends Effect {

    static {
        Skript.registerEffect(EffFlora.class, "make flora within %worldeditregion% [with density %-number%]");
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<Number> density;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        wrapper = (Expression<RegionWrapper>) exprs[0];
        density = (Expression<Number>) exprs[1];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        double density = this.density.getOptionalSingle(event).orElse(5).doubleValue();

        RunnableUtils.run(Runnables.getFloraRunnable(wrapper, density));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "make flora within " + wrapper.toString(event, debug) + " with density " + (density != null ? density.toString(event, debug) : "5");
    }
}
