package me.cheezburga.skwe.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
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

import static me.cheezburga.skwe.api.utils.Utils.locationFrom;

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
        WorldEditPlugin plugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        LocalSession session = WorldEdit.getInstance().getSessionManager().findByName(player.getName());
        World world = session.getSelectionWorld();
        try {
            if (world != null) {
                RegionSelector region = session.getRegionSelector(world);
                if (pos == 1)
                    return Utils.locationFrom(region.getPrimaryPosition(), BukkitAdapter.adapt(world));
                // TODO: POS 2
                // should this use a different method? getSelection maybe?
            }
        } catch (IncompleteRegionException e) {
            return null;
        }
        return null;
    }

    @Override
    @Nullable
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET || mode == ChangeMode.RESET || mode == ChangeMode.DELETE)
            return CollectionUtils.array(Location.class);
        return null;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
        return;
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
