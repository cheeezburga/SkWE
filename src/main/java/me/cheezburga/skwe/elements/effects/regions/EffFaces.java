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

@Name("Region - Create Faces")
@Description("Creates the faces (walls + ceiling + floor) of a given region using a given pattern.")
@Examples({
    "create the faces of {region} made out of \"50%%stone,30%%stone_bricks,20%%cobblestone\""
})
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class EffFaces extends SkWEEffect {

    static {
        Skript.registerEffect(EffFaces.class, "(create|place|make|generate) [the] faces (of|around) %worldeditregions% ([made] out of|with|using) [pattern] " + Utils.PATTERN_TYPES + Utils.LAZILY);
    }

    private Expression<RegionWrapper> wrappers;
    private Expression<?> prePattern;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrappers = (Expression<RegionWrapper>) exprs[0];
        prePattern = exprs[1];
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @Override
    protected void execute(Event event) {
        Pattern pattern = Utils.patternFrom(this.prePattern.getSingle(event));
        if (pattern == null) {
            error("The provided pattern was not set!", Utils.toHighlight(this.prePattern));
            return;
        }

        RegionWrapper[] wrappers = this.wrappers.getArray(event);
        if (wrappers.length < 1) {
            warning("No region(s) was provided!", Utils.toHighlight(this.wrappers));
            return;
        }

        for (RegionWrapper wrapper : wrappers) {
            RunnableUtils.run(Runnables.getFacesRunnable(wrapper, pattern), isBlocking());
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return new BlockingSyntaxStringBuilder(event, debug, isBlocking())
            .append("create faces of ", wrappers, " with pattern ", prePattern)
            .toString();
    }

}
