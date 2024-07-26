package me.cheezburga.skwe.elements.expressions.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprBlockCount extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprBlockCount.class, Number.class, ExpressionType.COMBINED,
                "[the] (count|number) of blocks in %worldeditregions% that match [mask] " + Utils.MASK_TYPES,
                "[the] (count|number) of " + Utils.MASK_TYPES + " [blocks] in %worldeditregions%");
    }

    private Expression<RegionWrapper> wrappers;
    private Expression<?> preMask;

    @Override
    @SuppressWarnings({"unchecked", "NullableProblems"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrappers = (Expression<RegionWrapper>) exprs[matchedPattern];
        preMask = exprs[matchedPattern ^ 1];
        return true;
    }

    @Override
    protected @Nullable Number[] get(Event event) {
        Object preMask = this.preMask.getSingle(event);
        if (preMask == null)
            return null;

        int count = 0;
        for (RegionWrapper wrapper : this.wrappers.getArray(event)) {
            count += wrapper.countBlocks(preMask);
        }

        return CollectionUtils.array(count);
    }

    @Override
    public boolean isSingle() {
        return wrappers.isSingle();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "the count of " + preMask.toString(event, debug) + " in " + wrappers.toString(event, debug);
    }
}
