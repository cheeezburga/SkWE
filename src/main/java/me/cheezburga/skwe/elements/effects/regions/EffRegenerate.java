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

public class EffRegenerate extends Effect {

    static {
        Skript.registerEffect(EffRegenerate.class, "regen[erate] %worldeditregion% [with seed %-number%] [biomes:while regen[erat]ing biomes]");
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<Number> seed;
    private boolean regenBiomes;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        wrapper = (Expression<RegionWrapper>) exprs[0];
        seed = (Expression<Number>) exprs[1];
        regenBiomes = parseResult.hasTag("biomes");
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        @Nullable Long seed = this.seed != null ? (Long) this.seed.getSingle(event) : null;

        RunnableUtils.run(Runnables.getRegenRunnable(wrapper, seed, regenBiomes));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "regen " + wrapper.toString(event, debug) + (seed != null ? " with seed " + seed.toString(event, debug) : "") + (regenBiomes ? " while regenerating biomes" : "");
    }
}
