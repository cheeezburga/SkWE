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
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import me.cheezburga.skwe.lang.BlockingSyntaxStringBuilder;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Hollow Out")
@Description({
    "Hollows out objects in a given region, using a given thickness and leaving behind a given pattern.",
    "The default thickness is 1, and the default pattern is just air."
})
@Examples({
    "hollow out {region} with thickness 2 leaving behind glass"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffHollow extends SkWEEffect {

    static {
        Skript.registerEffect(EffHollow.class, "hollow out %worldeditregions% [with thickness %-number%] [and] [(with pattern|leaving behind) " + Utils.PATTERN_TYPES_OPTIONAL + "]" + Utils.LAZILY);
    }

    private Expression<RegionWrapper> wrappers;
    private Expression<Number> thickness;
    private Expression<?> prePattern;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrappers = (Expression<RegionWrapper>) exprs[0];
        thickness = (Expression<Number>) exprs[1];
        prePattern = exprs[2];
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @Override
    protected void execute(Event event) {
        int thickness = (this.thickness == null) ? 1 : this.thickness.getOptionalSingle(event).orElse(1).intValue();

        Pattern pattern = null;
        if (this.prePattern != null)
            pattern = Utils.patternFrom(this.prePattern.getSingle(event));
        if (pattern == null)
            pattern = Utils.AIR_PATTERN;

        for (RegionWrapper wrapper : wrappers.getArray(event)) {
            RunnableUtils.run(Runnables.getHollowRunnable(wrapper, pattern, null, thickness), isBlocking()); // TODO: make this accept a mask if FAWE is found
        }

    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        BlockingSyntaxStringBuilder builder = new BlockingSyntaxStringBuilder(event, debug, isBlocking())
            .append("hollow out ", wrappers, " with thickness ")
            .append(thickness == null ? "1" : thickness);
        if (prePattern != null)
            builder.append(" with pattern ", prePattern);
        return builder.toString();
    }

}
