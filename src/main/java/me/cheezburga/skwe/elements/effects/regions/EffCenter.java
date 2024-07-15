package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Place Centre")
@Description("Places a block in the centre of a given region using a given pattern.")
@Examples({
    "create a block at the centre of {region} with bedrock"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffCenter extends Effect {

    static {
        Skript.registerEffect(EffCenter.class, "create [a] block at [the] cent(re|er) of %worldeditregion% with [pattern] " + Utils.PATTERN_TYPES);
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<?> prePattern;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrapper = (Expression<RegionWrapper>) exprs[0];
        prePattern = exprs[1];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        Pattern pattern = Utils.patternFrom(this.prePattern.getSingle(event));
        if (pattern == null)
            return;

        RunnableUtils.run(Runnables.getCenterRunnable(wrapper, pattern));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "set the center block in " + wrapper.toString(event, debug) + " to pattern " + prePattern.toString(event, debug);
    }
}
