package me.cheezburga.skwe.elements.effects.blocks;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.blocks.Runnables;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffReplace extends Effect {

    static {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit"))
            Skript.registerEffect(EffReplace.class,
                    Utils.PATTERN_PREFIX + " replace [all] blocks in %worldeditregion% that match [mask] " + Utils.MASK_TYPES + " with [pattern] " + Utils.PATTERN_TYPES,
            Utils.PATTERN_PREFIX + " replace all " + Utils.MASK_TYPES + " in %worldeditregion% with " + Utils.PATTERN_TYPES);
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<?> preMask, prePattern;

    @SuppressWarnings({"NullableProblems","unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
        this.wrapper = (Expression<RegionWrapper>) (matchedPattern == 0 ? exprs[0] : exprs[1]);
        this.preMask = matchedPattern == 0 ? exprs[1] : exprs[0];
        this.prePattern = exprs[2];
        return true;
    }

    @Override
    protected void execute(Event event) {
        if (wrapper == null || prePattern == null || preMask == null) return;

        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null) return;

        Object prePattern = this.prePattern.getSingle(event);
        Pattern pattern = Utils.patternFrom(prePattern);
        if (pattern == null) return;

        Object preMask = this.preMask.getSingle(event);
        if (preMask == null) return;

        RunnableUtils.run(Runnables.getReplaceRunnable(wrapper.world(), wrapper.region(), pattern, preMask));
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "replace blocks in " + wrapper.toString(event, debug) + " that match mask " + preMask.toString(event, debug) + " with pattern " + prePattern.toString(event, debug);
    }
}
