package me.cheezburga.skwe.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import me.cheezburga.skwe.SkWE;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static me.cheezburga.skwe.api.utils.Utils.patternPrefix;

public class EffReplace extends Effect {

    static {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit"))
            Skript.registerEffect(EffReplace.class,
                    "(use (world[ ]edit|we) to|world[ ]edit|we) replace [all] blocks (within|from) %location% (to|and) %location% that match [mask] %itemtype/string% with [pattern] %itemtype/string% [as %-player%]");
    }

    private Expression<Location> loc1, loc2;
    private Expression<?> preMask, prePattern;
    private Expression<Player> player;

    @SuppressWarnings({"NullableProblems","unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
        this.loc1 = (Expression<Location>) exprs[0];
        this.loc2 = (Expression<Location>) exprs[1];
        this.preMask = exprs[2];
        this.prePattern = exprs[3];
        this.player = (Expression<Player>) exprs[4];
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
        Object preMask = this.preMask.getSingle(event);
        Mask mask = Utils.maskFrom(preMask);
        if (pattern == null || mask == null)
            return;

        WorldEdit worldEdit = WorldEdit.getInstance();

        World world = BukkitAdapter.adapt(l1.getWorld());
        BlockVector3 min, max;
        CuboidRegion region = new CuboidRegion(world, min = Utils.bv3From(l1), max = Utils.bv3From(l2));
        int changed = -1;
        try (EditSession session = worldEdit.newEditSession(world)) {
            changed = session.replaceBlocks((Region) region, mask, pattern);
            SkWE.getInstance().getLocalSession().remember(session);
            if (this.player != null && this.player.getSingle(event) != null)
                worldEdit.getSessionManager().findByName(this.player.getSingle(event).getName()).remember(session);
        }
        Bukkit.getLogger().info("Replaced " + changed + " blocks between " + min + " and " + max + " with mask " + mask + " pattern " + pattern);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "fawe replace blocks";
    }
}
