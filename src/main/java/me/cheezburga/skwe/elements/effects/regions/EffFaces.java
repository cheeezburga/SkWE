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

public class EffFaces extends Effect {

    static {
        Skript.registerEffect(EffFaces.class, "create [the] faces (of|around) %worldeditregion% ([made] out of|with [pattern]) " + Utils.PATTERN_TYPES);
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<?> prePattern;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        wrapper = (Expression<RegionWrapper>) exprs[0];
        prePattern = exprs[1];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        Pattern pattern = Utils.patternFrom(prePattern.getSingle(event));
        if (pattern == null)
            return;

        RunnableUtils.run(Runnables.getFacesRunnable(wrapper, pattern));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create faces of " + wrapper.toString(event, debug) + " with pattern " + prePattern.toString(event, debug);
    }
}
