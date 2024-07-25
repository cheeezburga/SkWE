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
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Name("Create Spline")
@Description("Create a spline using a given region. This effect only works with convex polyhedral regions.")
@Examples("create a hollow spline using {locations::*} with pattern \"50%%quartz_block,50%%quartz_bricks\" and with thickness 2")
@Since("1.0.3")
@RequiredPlugins("WorldEdit")
public class EffSpline extends SkWEEffect {

    static {
        Skript.registerEffect(EffSpline.class,
                "(create|place|make|generate) [a] [:hollow] [rigid:rigid|straight] (curve|spline|line) (with|using|from) %locations% (with|using) [pattern] " + Utils.PATTERN_TYPES + " [[and] with thickness %-number%] " + Utils.LAZILY);
    }

    private Expression<Location> locs;
    private Expression<?> prePattern;
    private Expression<Number> thickness;
    private boolean hollow, rigid;

    @Override
    @SuppressWarnings({"unchecked", "NullableProblems"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        locs = (Expression<Location>) exprs[0];
        prePattern = exprs[1];
        thickness = (Expression<Number>) exprs[2];
        hollow = parseResult.hasTag("hollow");
        rigid = parseResult.hasTag("rigid");
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

        List<Location> locs = List.of(this.locs.getArray(event));
        if (locs.size() <= 1)
            return;

        World world = locs.getFirst().getWorld();

        RunnableUtils.run(Runnables.getSplineRunnable(world, Utils.toBlockVector3List(locs), pattern, thickness, hollow, rigid), isBlocking());
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create a " + (rigid ? "rigid " : "") + "spline using " + locs.toString(event, debug) + " using pattern " + prePattern.toString(event, debug) + " with thickness " + (thickness != null ? thickness.toString(event, debug) : "0") + (isBlocking() ? "" : " lazily");
    }
}
