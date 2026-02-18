package me.cheezburga.skwe.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.PatternWrapper;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Pattern")
@Description(
        "Gets a WorldEdit pattern from a string or itemtype."
)
@Examples({
        "set {simplePattern} to pattern of stone",
        "set {complexPattern} to pattern from \"33%%stone,33%%cobblestone,33%%stone_bricks\""
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class ExprPattern extends SimpleExpression<PatternWrapper> {

    static {
        Skript.registerExpression(ExprPattern.class, PatternWrapper.class, ExpressionType.COMBINED,
                "pattern (of|from|that matches) %string/itemtype%");
    }

    private Expression<?> prePattern;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.prePattern = exprs[0];
        return true;
    }

    @Override
    protected @Nullable PatternWrapper[] get(Event event) {
        Object prePattern = this.prePattern.getSingle(event);
        if (prePattern == null) {
            error("The provided pattern property was not set!", Utils.toHighlight(this.prePattern));
            return null;
        }
        Pattern pattern = Utils.patternFrom(prePattern);
        return (pattern == null) ? null : new PatternWrapper[]{new PatternWrapper(pattern, Utils.templateFrom(prePattern))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends PatternWrapper> getReturnType() {
        return PatternWrapper.class;
    }

    @Override
    public @NotNull String toString(Event event, boolean debug) {
        return "pattern from " + this.prePattern.toString(event, debug);
    }

}
