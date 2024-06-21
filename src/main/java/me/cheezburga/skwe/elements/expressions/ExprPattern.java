package me.cheezburga.skwe.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprPattern extends SimpleExpression<Pattern> {

    static {
        Skript.registerExpression(ExprPattern.class, Pattern.class, ExpressionType.COMBINED,
                "pattern (of|from|that matches) %string/itemtype%");
    }

    private Expression<?> prePattern;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.prePattern = exprs[0];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected Pattern @Nullable [] get(Event event) {
        Object prePattern = this.prePattern.getSingle(event);
        if (prePattern == null) return null;
        Pattern pattern = Utils.patternFrom(prePattern);
        return (pattern == null) ? null : new Pattern[]{pattern};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Pattern> getReturnType() {
        return Pattern.class;
    }

    @Override
    public @NotNull String toString(Event event, boolean debug) {
        return "pattern from " + this.prePattern.toString(event, debug);
    }

}
