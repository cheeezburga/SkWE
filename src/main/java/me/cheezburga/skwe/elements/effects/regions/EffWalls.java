package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Create Walls")
@Description("Creates the walls of a given region using a given pattern.")
@Examples({
    "create the walls of {region} made out of \"50%%stone,30%%stone_bricks,20%%cobblestone\""
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffWalls extends SkWEEffect {

    static {
        Skript.registerEffect(EffWalls.class, "create [the] walls (of|around) %worldeditregion% ([made] out of|with [pattern]) " + Utils.PATTERN_TYPES + Utils.LAZILY);
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<?> prePattern;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrapper = (Expression<RegionWrapper>) exprs[0];
        prePattern = exprs[1];
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        Pattern pattern = Utils.patternFrom(prePattern.getSingle(event));
        if (pattern == null)
            return;

        RunnableUtils.run(Runnables.getWallsRunnable(wrapper, pattern), isBlocking());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create walls around " + wrapper.toString(event, debug) + " with pattern " + prePattern.toString(event, debug) + (isBlocking() ? "" : " lazily");
    }
}
