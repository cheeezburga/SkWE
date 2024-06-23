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
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffSet extends Effect {

    static {
        Skript.registerEffect(EffSet.class,
                "(use (world[ ]edit|we) to|world[ ]edit|we) set blocks in %worldeditregion% to %itemtype/string%");
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<?> prePattern;

    @Override
    @SuppressWarnings({"NullableProblems","unchecked"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.wrapper = (Expression<RegionWrapper>) exprs[0];
        this.prePattern = exprs[1];
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

        RunnableUtils.run(Runnables.getSetRunnable(wrapper.getWorld(), wrapper.getRegion(), pattern));
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "set blocks in " + wrapper.toString(event, debug) + " to " + prePattern.toString(event, debug);
    }
}
