package me.cheezburga.skwe.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import me.cheezburga.skwe.SkWE;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static me.cheezburga.skwe.api.utils.Utils.patternPrefix;

public class EffSet extends Effect {

    static {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit"))
            Skript.registerEffect(EffSet.class,
                    "(use (world[ ]edit|we) to|world[ ]edit|we) set blocks (within|from) %location% (to|and) %location% to %itemtype/string% [as %-player%]");
    }

    private Expression<Location> loc1, loc2;
    private Expression<?> prePattern;
    private Expression<Player> player;

    @Override
    @SuppressWarnings({"NullableProblems","unchecked"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.loc1 = (Expression<Location>) exprs[0];
        this.loc2 = (Expression<Location>) exprs[1];
        this.prePattern = exprs[2];
        this.player = (Expression<Player>) exprs[3];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Location l1 = this.loc1.getSingle(event);
        Location l2 = this.loc2.getSingle(event);
        if (l1 == null || l2 == null || (l1.getWorld() != l2.getWorld()))
            return;

        Object prePattern = this.prePattern.getSingle(event);
        Pattern pattern = Utils.patternFrom(prePattern);
        if (pattern == null)
            return;

        WorldEdit worldEdit = WorldEdit.getInstance();

        World world = BukkitAdapter.adapt(l1.getWorld());
        CuboidRegion region = new CuboidRegion(world, Utils.bv3From(l1), Utils.bv3From(l2));
        try (EditSession session = worldEdit.newEditSession(world)) {
            session.setBlocks((Region) region, pattern);
            // should every single operation be remembered by the global session,
            // even if the player is specified? currently they are, but idk.
            SkWE.getInstance().getLocalSession().remember(session);
            if (this.player != null && this.player.getSingle(event) != null)
                worldEdit.getSessionManager().findByName(this.player.getSingle(event).getName()).remember(session);
        }
    }

    @Override
    @NotNull
    public String toString(@Nullable Event event, boolean debug) {
        return "use fawe to set blocks within " + loc1.toString(event, debug) + " and " + loc2.toString(event, debug) + " to " + prePattern.toString(event, debug);
    }
}
