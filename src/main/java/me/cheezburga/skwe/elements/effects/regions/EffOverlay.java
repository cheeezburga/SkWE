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
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Overlay")
@Description("Overlays the top level of blocks in a given region using a given pattern.")
@Examples({
    "overlay the top level of blocks in {region} with torch"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffOverlay extends SkWEEffect {

    static {
        Skript.registerEffect(EffOverlay.class, "overlay [the] [top level of] blocks (in|of) %worldeditregions% (with|using) [pattern] " + Utils.PATTERN_TYPES + Utils.LAZILY);
    }

    private Expression<RegionWrapper> wrappers;
    private Expression<?> prePattern;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrappers = (Expression<RegionWrapper>) exprs[0];
        prePattern = exprs[1];
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        Pattern pattern = Utils.patternFrom(this.prePattern.getSingle(event));
        if (pattern == null)
            return;

        for (RegionWrapper wrapper : wrappers.getArray(event)) {
            RunnableUtils.run(Runnables.getOverlayRunnable(wrapper, pattern), isBlocking());
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "overlay blocks in " + wrappers.toString(event, debug) + " with pattern " + prePattern.toString(event, debug) + (isBlocking() ? "" : " lazily");
    }
}
