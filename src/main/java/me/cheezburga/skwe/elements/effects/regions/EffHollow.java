package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffHollow extends Effect {

    static {
        Skript.registerEffect(EffHollow.class, "hollow out %worldeditregion% [with thickness %-number%] [with pattern %-itemtype/string%]");
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<Number> thickness;
    private Expression<?> prePattern;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        wrapper = (Expression<RegionWrapper>) exprs[0];
        thickness = (Expression<Number>) exprs[1];
        prePattern = exprs[2];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        Number thickness = this.thickness.getSingle(event);
        int t = thickness != null ? (int) thickness : 1;

        Object prePattern = this.prePattern.getSingle(event);
        Pattern pattern = prePattern == null ? Utils.AIR_PATTERN : Utils.patternFrom(prePattern);

        RunnableUtils.run(Runnables.getHollowRunnable(wrapper, pattern, t));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "hollow out " + wrapper.toString(event, debug) + " with thickness " + (thickness != null ? thickness.toString(event, debug) : "1") + (prePattern != null ? " with pattern " + prePattern.toString(event, debug) : "");
    }
}
