package me.cheezburga.skwe.elements.effects.blocks;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.blocks.Runnables;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Set Blocks")
@Description("Sets the blocks in a region with a given pattern.")
@Examples({
        "use we to set blocks in {region} to \"33%%red_wool,33%blue_wool,33%green_wool\""
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffSet extends SkWEEffect {

    static {
        Skript.registerEffect(EffSet.class,
                 Utils.PATTERN_PREFIX + " set blocks in %worldeditregion% to " + Utils.PATTERN_TYPES + " " + Utils.LAZILY);
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<?> prePattern;

    @Override
    @SuppressWarnings({"NullableProblems","unchecked"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.wrapper = (Expression<RegionWrapper>) exprs[0];
        this.prePattern = exprs[1];
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @Override
    protected void execute(@NotNull Event event) {
        if (wrapper == null || prePattern == null) return;

        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null) return;

        Object prePattern = this.prePattern.getSingle(event);
        Pattern pattern = Utils.patternFrom(prePattern);
        if (pattern == null) return;

        RunnableUtils.run(Runnables.getSetRunnable(wrapper.world(), wrapper.region(), pattern), isBlocking());
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "set blocks in " + wrapper.toString(event, debug) + " to " + prePattern.toString(event, debug) + (isBlocking() ? "" : " lazily");
    }
}
