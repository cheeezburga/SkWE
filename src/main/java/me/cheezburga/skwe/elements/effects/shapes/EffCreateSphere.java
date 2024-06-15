package me.cheezburga.skwe.elements.effects.shapes;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.SkWE;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffCreateSphere extends Effect {

    static {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit"))
            Skript.registerEffect(EffCreateSphere.class,
                    "create [a] [:hollow] (sphere|ellipsoid) ([made] out of|with [pattern]) %-itemtype/string% [with radius %-number%] at %locations%");
    }

    private boolean hollow;
    private Expression<?> prePattern;
    private Expression<Number> radius;
    private Expression<Location> locations;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        hollow = parseResult.hasTag("hollow");
        prePattern = exprs[0];
        radius = (Expression<Number>) exprs[1];
        locations = (Expression<Location>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Number r = radius.getOptionalSingle(event).orElse(5); //TODO: replace with config
        Pattern pattern = Utils.patternFrom(prePattern.getSingle(event));
        if (pattern == null)
            return;

        WorldEdit we = WorldEdit.getInstance();

        for (Location loc : locations.getArray(event)) {
            Runnable runnable = () -> {
                try (EditSession session = we.newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
                    session.makeSphere(Utils.blockVector3From(loc), pattern, r.doubleValue(), !hollow);

                    SkWE.getInstance().getLocalSession().remember(session);
                }
            };
            //TODO: instance check
            Bukkit.getScheduler().runTaskAsynchronously(SkWE.getInstance(), runnable);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create a " + (hollow ? "hollow " : "") + "sphere with radius " + radius.toString(event, debug) + " at locations " + locations.toString(event, debug);
    }
}
