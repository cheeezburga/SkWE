package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Naturalize")
@Description("Naturalizes a given region. This just changes the top 3 layers of blocks into grass/dirt, and the bottom layers into stone.")
@Examples({
    "naturalize {region}"
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffNaturalize extends Effect {

    static {
        Skript.registerEffect(EffNaturalize.class, "naturalize %worldeditregion%");
    }

    private Expression<RegionWrapper> wrapper;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrapper = (Expression<RegionWrapper>) exprs[0];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        RunnableUtils.run(Runnables.getNaturalizeRunnable(wrapper));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "naturalize " + wrapper.toString(event, debug);
    }
}
