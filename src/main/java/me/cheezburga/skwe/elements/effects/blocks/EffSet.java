package me.cheezburga.skwe.elements.effects.blocks;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.fastasyncworldedit.core.regions.PolyhedralRegion;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import me.cheezburga.skwe.SkWE;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.blocks.Runnables;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class EffSet extends Effect {

    static {
        Skript.registerEffect(EffSet.class,
                "(use (world[ ]edit|we) to|world[ ]edit|we) set blocks in %worldeditregion% to %itemtype/string%");
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<?> prePattern;

    @Override
    @SuppressWarnings({"NullableProblems","unchecked"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.wrapper = (Expression<RegionWrapper>) exprs[0];
        this.prePattern = exprs[1];
        return true;
    }

    @Override
    protected void execute(@NotNull Event event) {
        if (wrapper == null || prePattern == null) return;

        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null) return;

        Object prePattern = this.prePattern.getSingle(event);
        Pattern pattern = Utils.patternFrom(prePattern);
        if (pattern == null) return;

        PolyhedralRegion r = (PolyhedralRegion) wrapper.getRegion();
        Bukkit.getServer().broadcast(Component.text("Size: " + r.getVertices().size() + ", volume: " + r.getVolume()));

        RunnableUtils.run(Runnables.getSetRunnable(wrapper.getWorld(), wrapper.getRegion(), pattern));
    }

    @Override
    @NotNull
    public String toString(@Nullable Event event, boolean debug) {
        return "use fawe to set blocks in " + wrapper.toString(event, debug) + " to " + prePattern.toString(event, debug);
    }
}
