package me.cheezburga.skwe.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import me.cheezburga.skwe.SkWE;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class EffUndoRedo extends Effect {

    static {
        Skript.registerEffect(EffUndoRedo.class,
                "(:un|re)do [the] last [%number%] operation[s] [[performed] (by|from) %-players%]",
                "(:un|re)do %players%'[s] last [%number%] operation[s]");
    }

    private boolean undo;
    private Expression<Player> players;
    private Expression<Number> number;

    @SuppressWarnings({"NullableProblems","unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.undo = parseResult.hasTag("un");
        this.players = (Expression<Player>) (matchedPattern == 0 ? exprs[1] : exprs[0]);
        this.number = (Expression<Number>) (matchedPattern == 0 ? exprs[0] : exprs[1]);
        return true;
    }

    @Override
    protected void execute(Event event) {
        int times = Math.max(1, number.getOptionalSingle(event).orElse(1).intValue());

        WorldEdit worldEdit = WorldEdit.getInstance();

        Set<Actor> actors = new HashSet<>();
        if (players != null) {
            for (Player p : players.getArray(event))
                actors.add(BukkitAdapter.adapt(p));
        } else actors.add(BukkitAdapter.adapt(Bukkit.getConsoleSender()));

        for (Actor actor : actors) {
            // int doneCount = 0;
            LocalSession session = (actor instanceof com.sk89q.worldedit.entity.Player) ?
                    worldEdit.getSessionManager().findByName(actor.getName()) : SkWE.getInstance().getLocalSession();

            for (int i = 0; i < times; ++i) {
                BlockBag bag = actor instanceof com.sk89q.worldedit.entity.Player ? session.getBlockBag((com.sk89q.worldedit.entity.Player) actor) : null;
                EditSession done;
                if (undo)
                    done = session.undo(bag, actor);
                else
                    done = session.redo(bag, actor);
                if (done != null) {
                    // doneCount++;
                    worldEdit.flushBlockBag(actor, done);
                } else break;
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "fawe undo operations";
    }
}
