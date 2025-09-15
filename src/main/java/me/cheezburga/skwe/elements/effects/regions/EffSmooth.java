package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.mask.Mask;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import me.cheezburga.skwe.lang.BlockingSyntaxStringBuilder;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Smooth")
@Description({
    "Smooths the area within a given region a certain number of times. The default number of times is 1.",
    "You can choose to only smooth blocks that match a certain mask."
})
@Examples({
    "smooth all grass block in {region} 10 times"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffSmooth extends SkWEEffect {

    static {
        Skript.registerEffect(EffSmooth.class, "smooth [mask:(blocks that match|all) " + Utils.MASK_TYPES_OPTIONAL + " in] %worldeditregions% [%-number% time[s]]" + Utils.LAZILY);
    }

    private Expression<?> preMask;
    private Expression<RegionWrapper> wrappers;
    private Expression<Number> iterations;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        preMask = exprs[0];
        wrappers = (Expression<RegionWrapper>) exprs[1];
        iterations = (Expression<Number>) exprs[2];
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @Override
    protected void execute(Event event) {
        int iterations = (this.iterations == null) ? 1 : this.iterations.getOptionalSingle(event).orElse(1).intValue();

        Mask mask = null;
        if (preMask != null) {
            mask = Utils.maskFrom(preMask.getSingle(event), null);
            // does this mask need more context (wrapper.world()?)
            if (mask == null)
                return;
        }

        for (RegionWrapper wrapper : wrappers.getArray(event)) {
            RunnableUtils.run(Runnables.getSmoothRunnable(wrapper, iterations, mask), isBlocking());
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        BlockingSyntaxStringBuilder builder = new BlockingSyntaxStringBuilder(event, debug, isBlocking())
            .append("smooth ", wrappers)
            .append(" ", iterations == null ? "1" : iterations, " times");
        if (preMask != null)
            builder.append(" with mask ", preMask);
        return builder.toString();
    }

}
