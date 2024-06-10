package me.cheezburga.skwe.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffUndo extends Effect {

    static {
        Skript.registerEffect(EffUndo.class,
                "undo [the] last [%number%] operation[s] [[performed] (by|from) %players%]",
                "undo %players%'[s] last [%number%] operation[s]");
    }

    private Expression<Player> players;
    private Expression<Number> number;

    @SuppressWarnings({"NullableProblems","unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
        this.players = (Expression<Player>) (matchedPattern == 0 ? exprs[1] : exprs[0]);
        this.number = (Expression<Number>) (matchedPattern == 0 ? exprs[0] : exprs[1]);
        return true;
    }

    @Override
    protected void execute(Event event) {
        return;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "fawe undo operations";
    }
}
