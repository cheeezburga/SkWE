package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.mask.Mask;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Smooth")
@Description({
    "Smooths the area within a given region a certain number of times. The default number of times is 1.",
    "You can choose to only smooth blocks that match a certain mask."
})
@Examples({
    "smooth {region} 10 times"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffSmooth extends SkWEEffect {

    static {
        Skript.registerEffect(EffSmooth.class, "smooth %worldeditregion% [%-number% time[s]] [with mask " + Utils.MASK_TYPES_OPTIONAL + "]" + Utils.LAZILY);
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
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        int iterations = (this.iterations == null) ? 1 : this.iterations.getOptionalSingle(event).orElse(1).intValue();

        Mask mask = null;
        if (preMask != null) {
            mask = Utils.maskFrom(preMask.getSingle(event), null);
            // does this mask need more context (wrapper.world()?)
            if (mask == null)
                return;
        }

        RunnableUtils.run(Runnables.getSmoothRunnable(wrapper, iterations, mask), isBlocking());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "smooth " + wrapper.toString(event, debug) + " " + (iterations != null ? iterations.toString(event, debug) : "1") + " times" + (preMask != null ? " with mask " + preMask.toString(event, debug) : "") + (isBlocking() ? "" : " lazily");
    }
}
