package me.cheezburga.skwe.elements.expressions.regions.create;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.regions.Region;
import me.cheezburga.skwe.api.utils.regions.Getters;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprConvexPolyRegion extends SimpleExpression<RegionWrapper> {

    static {
        Skript.registerExpression(ExprConvexPolyRegion.class, RegionWrapper.class, ExpressionType.COMBINED,
                "[a] [new] convex [poly[hedral]] region (at|with|using) %locations%");
    }

    private Expression<Location> locs;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.locs = (Expression<Location>) exprs[0];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected @Nullable RegionWrapper[] get(Event event) {
        if (this.locs == null) return null;

        Location[] locs = this.locs.getArray(event);
        if (locs.length == 0) return null;

        Region region = Getters.getConvexPolyRegion(locs);
        RegionWrapper wrapper = new RegionWrapper(region, locs[0].getWorld());

        return new RegionWrapper[]{wrapper};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends RegionWrapper> getReturnType() {
        return RegionWrapper.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "convex polyhedral region at " + locs.toString(event, debug);
    }

}
