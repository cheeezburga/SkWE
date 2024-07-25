package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.ConvexPolyhedralRegion;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Create Spline")
@Description("Create a spline using a given region. This effect only works with convex polyhedral regions.")
@Examples("create the hollow spline of {convex} with pattern \"50%%quartz_block,50%%quartz_bricks\" and with thickness 2")
@Since("1.0.3")
@RequiredPlugins("WorldEdit")
public class EffCurve extends SkWEEffect {

    static {
        // TODO: make this not rely on a region. should be able to make it accept just locations as well?
        Skript.registerEffect(EffCurve.class,
                "(create|place|make|generate) [a|the] [:hollow] (curve|spline) (across|of|using) %worldeditregions% (with|using) [pattern] " + Utils.PATTERN_TYPES + " [[and] with thickness %-number%] " + Utils.LAZILY,
                "(create|place|make|generate) %worldeditregions%'[s] [:hollow] (curve|spline) (with|using) [pattern] " + Utils.PATTERN_TYPES + " [[and] with thickness %-number%] " + Utils.LAZILY);
    }

    private Expression<RegionWrapper> wrappers;
    private Expression<?> prePattern;
    private Expression<Number> thickness;
    private boolean hollow;

    @Override
    @SuppressWarnings({"unchecked", "NullableProblems"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        wrappers = (Expression<RegionWrapper>) exprs[0];
        prePattern = exprs[1];
        thickness = (Expression<Number>) exprs[2];
        hollow = parseResult.hasTag("hollow");
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected void execute(Event event) {
        Pattern pattern = Utils.patternFrom(this.prePattern.getSingle(event));
        if (pattern == null)
            return;

        int thickness = (this.thickness == null) ? 0 : this.thickness.getOptionalSingle(event).orElse(0).intValue();

        for (RegionWrapper wrapper : wrappers.getArray(event)) {
            if (wrapper.region() instanceof ConvexPolyhedralRegion)
                RunnableUtils.run(Runnables.getCurveRunnable(wrapper, pattern, thickness, hollow), isBlocking());
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create the spline of " + wrappers.toString(event, debug) + " using pattern " + prePattern.toString(event, debug) + " with thickness " + (thickness != null ? thickness.toString(event, debug) : "0");
    }
}
