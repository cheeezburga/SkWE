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

public class ExprCylinderRegion extends SimpleExpression<RegionWrapper> {

    static {
        Skript.registerExpression(ExprCylinderRegion.class, RegionWrapper.class, ExpressionType.COMBINED,
                "[a] [new] cyl[ind(er|rical)] region at %location% with radi(i|us) %numbers% [and] with height %number%");
    }

    private Expression<Location> center;
    private Expression<Number> radii, height;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.center = (Expression<Location>) exprs[0];
        this.radii = (Expression<Number>) exprs[1];
        this.height = (Expression<Number>) exprs[2];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected @Nullable RegionWrapper[] get(Event event) {
        if (this.center == null || this.radii == null || this.height == null) return null;

        Location center = this.center.getSingle(event);
        if (center == null) return null;

        Number height = this.height.getSingle(event);
        if (height == null) return null;

        double rX, rZ;
        Number[] radii = this.radii.getArray(event);
        if (radii.length == 0) return null;
        rX = radii[0].doubleValue();
        rZ = (radii.length == 2) ? radii[1].doubleValue() : rX;

        World world = center.getWorld();
        Region region = Getters.getCylinderRegion(center, rX, rZ, height.intValue());
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
        return "cylinder region at " + center.toString(event, debug) + " with radii " + radii.toString(event, debug) + " and height " + height.toString(event,debug);
    }

}
