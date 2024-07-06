package me.cheezburga.skwe.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.mask.Mask;
import me.cheezburga.skwe.api.utils.MaskWrapper;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprMask extends SimpleExpression<MaskWrapper> {

    static {
        Skript.registerExpression(ExprMask.class, MaskWrapper.class, ExpressionType.COMBINED,
                "mask (of|from|that matches) %string/itemtype%");
    }

    private Expression<?> preMask;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.preMask = exprs[0];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected @Nullable MaskWrapper[] get(Event event) {
        Object preMask = this.preMask.getSingle(event);
        if (preMask == null) return null;
        Mask mask = Utils.maskFrom(preMask, null); // TODO: should this use a context instead of just using null?
        return (mask == null) ? null : new MaskWrapper[]{new MaskWrapper(mask, Utils.templateFrom(preMask))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends MaskWrapper> getReturnType() {
        return MaskWrapper.class;
    }

    @Override
    public @NotNull String toString(Event event, boolean debug) {
        return "mask from " + this.preMask.toString(event, debug);
    }
}
