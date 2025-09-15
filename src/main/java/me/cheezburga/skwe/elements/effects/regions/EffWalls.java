package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import me.cheezburga.skwe.lang.BlockingSyntaxStringBuilder;
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
        Skript.registerEffect(EffWalls.class, "create [the] walls (of|around) %worldeditregions% ([made] out of|with|using) [pattern] " + Utils.PATTERN_TYPES + Utils.LAZILY);
    }

    private Expression<RegionWrapper> wrappers;
    private Expression<?> prePattern;

    @Override
    @SuppressWarnings({"unchecked"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrappers = (Expression<RegionWrapper>) exprs[0];
        prePattern = exprs[1];
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @Override
    protected void execute(Event event) {
        Pattern pattern = Utils.patternFrom(prePattern.getSingle(event));
        if (pattern == null)
            return;

        for (RegionWrapper wrapper : wrappers.getArray(event)) {
            RunnableUtils.run(Runnables.getWallsRunnable(wrapper, pattern), isBlocking());
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return new BlockingSyntaxStringBuilder(event, debug, isBlocking())
            .append("create walls around ", wrappers, " with pattern ", prePattern)
            .toString();
    }

}
