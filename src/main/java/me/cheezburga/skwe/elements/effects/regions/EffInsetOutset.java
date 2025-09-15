package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Inset/Outset")
@Description("Insets or outsets a region by a given number of blocks. Can optionally only perform the operation vertically (up and down) or horizontally (north, south, east, and west).")
@Examples("outset {region} by 10 blocks")
@Since("1.0.4")
@RequiredPlugins("WorldEdit")
public class EffInsetOutset extends SkWEEffect {

    static {
        Skript.registerEffect(EffInsetOutset.class,
            "(:in|out)set %worldeditregions% by %number% [blocks] [1:vertically|2:horizontally]");
    }

    private Expression<RegionWrapper> wrappers;
    private Expression<Number> distance;
    private boolean in;
    private int direction;

    @Override
    @SuppressWarnings({"unchecked"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        wrappers = (Expression<RegionWrapper>) exprs[0];
        distance = (Expression<Number>) exprs[1];
        in = parseResult.hasTag("in");
        direction = parseResult.mark;
        return true;
    }

    @Override
	protected void execute(Event event) {
        int distance = (this.distance == null) ? 1 : this.distance.getOptionalSingle(event).orElse(1).intValue();

        for (RegionWrapper wrapper : wrappers.getArray(event)) {
            if (in) {
                wrapper.inset(distance, this.direction);
            } else {
                wrapper.outset(distance, this.direction);
            }
        }
    }

    @Override
    @SuppressWarnings({"ConstantConditions"})
    public String toString(@Nullable Event event, boolean debug) {
        SyntaxStringBuilder builder = new SyntaxStringBuilder(event, debug);
        builder.append(in ? "in" : "out", "set", wrappers, " by ", (distance == null ? "1" : distance));
        if (this.direction == 0) {
            return builder.toString();
        } else {
            String direction = this.direction == 1 ? "vertical" : "horizontal";
            return builder.append(" in only the ", direction, " direction").toString();
        }
    }

}
