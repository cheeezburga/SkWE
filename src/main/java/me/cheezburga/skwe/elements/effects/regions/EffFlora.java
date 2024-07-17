package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Flora")
@Description("Generates flora in a given region, with a given density. The default density is 5.")
@Examples({
    "make flora within {region} with density 10"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffFlora extends SkWEEffect {

    static {
        Skript.registerEffect(EffFlora.class, "make flora within %worldeditregion% [with density %-number%]" + Utils.LAZILY);
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<Number> density;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrapper = (Expression<RegionWrapper>) exprs[0];
        density = (Expression<Number>) exprs[1];
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        double density = (this.density == null) ? 5 : this.density.getOptionalSingle(event).orElse(5).doubleValue();

        RunnableUtils.run(Runnables.getFloraRunnable(wrapper, density), isBlocking());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "make flora within " + wrapper.toString(event, debug) + " with density " + (density != null ? density.toString(event, debug) : "5") + (isBlocking() ? "" : " lazily");
    }
}
