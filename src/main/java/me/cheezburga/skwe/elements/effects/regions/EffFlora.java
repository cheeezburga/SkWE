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
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import me.cheezburga.skwe.lang.BlockingSyntaxStringBuilder;
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
        Skript.registerEffect(EffFlora.class, "(create|place|make|generate) flora [with]in %worldeditregions% [with density %-number%]" + Utils.LAZILY);
    }

    private Expression<RegionWrapper> wrappers;
    private Expression<Number> density;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrappers = (Expression<RegionWrapper>) exprs[0];
        density = (Expression<Number>) exprs[1];
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @Override
    protected void execute(Event event) {
        RegionWrapper[] wrappers = this.wrappers.getArray(event);
        if (wrappers.length < 1) {
            warning("No region(s) was provided!", Utils.toHighlight(this.wrappers));
            return;
        }

        double density = (this.density == null) ? 5 : this.density.getOptionalSingle(event).orElse(5).doubleValue();

        for (RegionWrapper wrapper : wrappers) {
            RunnableUtils.run(Runnables.getFloraRunnable(wrapper, density), isBlocking());
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return new BlockingSyntaxStringBuilder(event, debug, isBlocking())
            .append("generate flora within ", wrappers, " with density ")
            .append(density == null ? "5" : density)
            .toString();
    }

}
