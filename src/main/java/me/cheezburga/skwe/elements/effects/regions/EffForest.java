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
import com.sk89q.worldedit.util.TreeGenerator.TreeType;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import me.cheezburga.skwe.lang.BlockingSyntaxStringBuilder;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Forest")
@Description({
        "Generates a forest of a specific tree type in a given region, with a given density. The default density is 5.",
        "Note that this effect uses a new tree type, not the Skript one."
})
@Examples({
        "generate a tall mangrove forest within {region} with density 10"
})
@Since("1.1.2")
@RequiredPlugins("WorldEdit")
public class EffForest extends SkWEEffect {

    static {
        Skript.registerEffect(EffForest.class, "generate [a] %treetype% forest [with]in %worldeditregions% [with density %-number%]" + Utils.LAZILY);
    }

    private Expression<TreeType> treeType;
    private Expression<RegionWrapper> wrappers;
    private Expression<Number> density;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        treeType = (Expression<TreeType>) exprs[0];
        wrappers = (Expression<RegionWrapper>) exprs[1];
        density = (Expression<Number>) exprs[2];
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @Override
    protected void execute(Event event) {
        TreeType treeType = this.treeType.getSingle(event);
        double density = (this.density == null) ? 5 : this.density.getOptionalSingle(event).orElse(5).doubleValue();

        for (RegionWrapper wrapper : wrappers.getArray(event)) {
            RunnableUtils.run(Runnables.getForestRunnable(wrapper, treeType, density), isBlocking());
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return new BlockingSyntaxStringBuilder(event, debug, isBlocking())
            .append("generate ", treeType, " forest within ", wrappers, " with density ")
            .append(density == null ? "5" : density)
            .toString();
    }

}
