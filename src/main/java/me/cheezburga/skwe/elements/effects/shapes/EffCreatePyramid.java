package me.cheezburga.skwe.elements.effects.shapes;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
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

public class EffCreatePyramid extends Effect {

    static {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit"))
            Skript.registerEffect(EffCreatePyramid.class,
                    "create [a] [:hollow] pyramid ([made] out of|with [pattern]) %-itemtype/string% [with size %-number%] at %locations%");
    }

    private boolean hollow;
    private Expression<?> prePattern;
    private Expression<Number> size;
    private Expression<Location> locations;

    @SuppressWarnings({"NullableProblems","unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        hollow = parseResult.hasTag("hollow");
        prePattern = exprs[0];
        size = (Expression<Number>) exprs[1];
        locations = (Expression<Location>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Number r = size.getOptionalSingle(event).orElse(5); //TODO: replace with config
        Pattern pattern = Utils.patternFrom(prePattern.getSingle(event));
        if (pattern == null)
            return;

        WorldEdit we = WorldEdit.getInstance();

        for (Location loc : locations.getArray(event)) {
            Runnable runnable = () -> {
                try (EditSession session = we.newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
                    session.makePyramid(Utils.blockVector3From(loc), pattern, r.intValue(), !hollow);

                    SkWE.getInstance().getLocalSession().remember(session);
                }
            };
            //TODO: instance check
            Bukkit.getScheduler().runTaskAsynchronously(SkWE.getInstance(), runnable);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create a " + (hollow ? "hollow " : "") + "pyramid with size " + size.toString(event, debug) + " at locations " + locations.toString(event, debug);
    }
}
