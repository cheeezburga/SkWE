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

@Name("Region - Naturalize")
@Description("Naturalizes a given region. This just changes the top 3 layers of blocks into grass/dirt, and the bottom layers into stone.")
@Examples({
    "naturalize {region}"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffNaturalize extends SkWEEffect {

    static {
        Skript.registerEffect(EffNaturalize.class, "naturalize %worldeditregions%" + Utils.LAZILY);
    }

    private Expression<RegionWrapper> wrappers;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrappers = (Expression<RegionWrapper>) exprs[0];
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

        for (RegionWrapper wrapper : wrappers) {
            RunnableUtils.run(Runnables.getNaturalizeRunnable(wrapper), isBlocking());
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return new BlockingSyntaxStringBuilder(event, debug, isBlocking())
            .append("naturalize ", wrappers)
            .toString();
    }

}
