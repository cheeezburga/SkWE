package me.cheezburga.skwe.elements.expressions.regions;

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
import org.bukkit.World;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprCuboidRegion extends SimpleExpression<RegionWrapper> {

    static {
        Skript.registerExpression(ExprCuboidRegion.class, RegionWrapper.class, ExpressionType.COMBINED,
                "[a] [new] [cuboid] region (between|within) %location% and %location%");
    }

    private Expression<Location> loc1;
    private Expression<Location> loc2;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.loc1 = (Expression<Location>) exprs[0];
        this.loc2 = (Expression<Location>) exprs[1];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected @Nullable RegionWrapper[] get(Event event) {
        if (this.loc1 == null || this.loc2 == null) return null;

        Location loc1 = this.loc1.getSingle(event);
        Location loc2 = this.loc2.getSingle(event);
        if (loc1 == null || loc2 == null || loc1.getWorld() != loc2.getWorld()) return null;

        World world = loc1.getWorld();
        Region region = Getters.getCuboidRegion(loc1, loc2, world);
        RegionWrapper wrapper = new RegionWrapper(region, world);

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
        return "cuboid region between " + loc1.toString(event, debug) + " and " + loc2.toString(event, debug);
    }

}
