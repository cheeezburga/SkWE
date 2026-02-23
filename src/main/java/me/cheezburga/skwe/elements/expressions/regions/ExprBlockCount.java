package me.cheezburga.skwe.elements.expressions.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
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

@Name("Region - Block Count")
@Description("Gets the number of a certain type of block in a region. Can use a mask to specify more than one kind of block.")
@Examples({
    "set {stone} to the number of stone in {region}",
    "set {multiple} to the number of blocks in {region} that match \"stone,diamond_block,oak_planks\""
})
@Since("1.0.4")
@RequiredPlugins("WorldEdit")
public class ExprBlockCount extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprBlockCount.class, Number.class, ExpressionType.COMBINED,
            "[the] (count|number) of blocks in %worldeditregions% that match [mask] " + Utils.MASK_TYPES,
            "[the] (count|number) of " + Utils.MASK_TYPES + " [blocks] in %worldeditregions%");
    }

    private Expression<RegionWrapper> wrappers;
    private Expression<?> preMask;

    @Override
    @SuppressWarnings({"unchecked"})
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
    @SuppressWarnings({"ConstantConditions"})
    public String toString(@Nullable Event event, boolean debug) {
        return "the count of " + preMask.toString(event, debug) + " in " + wrappers.toString(event, debug);
    }

}
