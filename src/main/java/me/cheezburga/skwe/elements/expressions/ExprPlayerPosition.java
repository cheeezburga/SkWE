package me.cheezburga.skwe.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.world.World;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprPlayerPosition extends SimplePropertyExpression<Player, Location> {

    static {
        register(ExprPlayerPosition.class, Location.class, "pos[ition] (1:(1|one)|2:(2|two))", "players");
    }

    private int pos;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pos = parseResult.mark;
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public Location convert(Player player) {
        WorldEdit worldEdit = WorldEdit.getInstance();
        try {
            LocalSession session = worldEdit.getSessionManager().findByName(player.getName());
            if (session != null) {
                World world = session.getSelectionWorld();
                if (world != null) {
                    CuboidRegion region = (CuboidRegion) session.getRegionSelector(world).getIncompleteRegion();
                    if (pos == 1) {
                        return Utils.locationFrom(region.getPos1(), BukkitAdapter.adapt(world));
                    }
                    else if (pos == 2) {
                        return Utils.locationFrom(region.getPos2(), BukkitAdapter.adapt(world));
                    }
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public @Nullable Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET || mode == ChangeMode.RESET || mode == ChangeMode.DELETE)
            return CollectionUtils.array(Location.class);
        return null;
    }

    @SuppressWarnings({"NullableProblems", "ConstantValue"})
    @Override
    public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
        Location newLoc = (delta != null && delta[0] instanceof Location loc) ? loc : null;
        WorldEdit worldEdit = WorldEdit.getInstance();
        for (Player player : getExpr().getArray(event)) {
            try {
                LocalSession session = worldEdit.getSessionManager().findByName(player.getName());
                if (session != null) {
                    World world = session.getSelectionWorld();
                    if (world != null) {
                        CuboidRegion region = (CuboidRegion) session.getSelection(world);
                        if (pos == 1)
                            region.setPos1(Utils.blockVector3From(newLoc));
                        else if (pos == 2)
                            region.setPos2(Utils.blockVector3From(newLoc));
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    @Override
    public @NotNull Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "position " + pos + "of " + getExpr().toString();
    }
}
