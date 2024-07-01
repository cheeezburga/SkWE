package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.mask.Mask;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffSmooth extends Effect {

    static {
        Skript.registerEffect(EffSmooth.class, "smooth %worldeditregion% [%-number% times] [with mask %-itemtype/string%]");
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<Number> iterations;
    private Expression<?> preMask;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrapper = (Expression<RegionWrapper>) exprs[0];
        iterations = (Expression<Number>) exprs[1];
        preMask = exprs[2];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        Number iterations = this.iterations.getSingle(event);
        if (iterations == null)
            iterations = 1;

        Mask mask = null;
        if (preMask != null)
            mask = Utils.maskFrom(preMask.getSingle(event), null);
            // does this mask need more context (wrapper.world()?)

        RunnableUtils.run(Runnables.getSmoothRunnable(wrapper, (int) iterations, mask));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "smooth " + wrapper.toString(event, debug) + " " + (iterations != null ? iterations.toString(event, debug) : "1") + " times" + (preMask != null ? " with mask " + preMask.toString(event, debug) : "");
    }
}
