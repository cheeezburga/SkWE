package me.cheezburga.skwe.elements.expressions.regions.properties;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.fastasyncworldedit.core.regions.PolyhedralRegion;
import com.sk89q.worldedit.regions.ConvexPolyhedralRegion;
import com.sk89q.worldedit.regions.Region;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprVertices extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprVertices.class, Location.class, ExpressionType.COMBINED,
                "[the] region vert(ices|exes) of %worldeditregion%", "%worldeditregion%'[s] vert(ices|exes)");
    }

    private Expression<RegionWrapper> wrapper;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrapper = (Expression<RegionWrapper>) exprs[0];
        return false;
    }

    @Override
    protected @Nullable Location[] get(Event event) {
        if (this.wrapper == null) return null;

        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null) return null;

        Region region = wrapper.region();
        World world = wrapper.world();

        if (region instanceof ConvexPolyhedralRegion convex)
            return convex.getVertices().stream().map(vector -> Utils.locationFrom(vector, world)).toList().toArray(Location[]::new);
        else if (region instanceof PolyhedralRegion poly)
            return poly.getVertices().stream().map(vector -> Utils.locationFrom(vector, world)).toList().toArray(Location[]::new);
        return null;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "vertices of " + wrapper.toString(event, debug);
    }
}
