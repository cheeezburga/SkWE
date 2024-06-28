package me.cheezburga.skwe.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.mask.Mask;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprMask extends SimpleExpression<Mask> {

    static {
        Skript.registerExpression(ExprMask.class, Mask.class, ExpressionType.COMBINED,
                "mask (of|from|that matches) %string/itemtype%");
    }

    private Expression<?> preMask;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.preMask = exprs[0];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected Mask @Nullable [] get(Event event) {
        Object preMask = this.preMask.getSingle(event);
        if (preMask == null) return null;
        Mask mask = Utils.maskFrom(preMask, null);
        return (mask == null) ? null : new Mask[]{mask};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Mask> getReturnType() {
        return Mask.class;
    }

    @Override
    public @NotNull String toString(Event event, boolean debug) {
        return "pattern from " + this.preMask.toString(event, debug);
    }
}
