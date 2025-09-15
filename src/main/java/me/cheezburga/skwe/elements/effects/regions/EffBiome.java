package me.cheezburga.skwe.elements.effects.regions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.biome.BiomeType;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.regions.Runnables;
import me.cheezburga.skwe.lang.BlockingSyntaxStringBuilder;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.block.Biome;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Region - Biome")
@Description("Sets the biome of given region.")
@Examples("set biome of {region} to (random element out of all biomes)")
@Since("1.0.3")
@RequiredPlugins("WorldEdit")
public class EffBiome extends SkWEEffect {

    static {
        // TODO: add the ability to set the biome at locations as well? would just create a cuboid region with pos,pos
        Skript.registerEffect(EffBiome.class, "set biome of %worldeditregions% to %biome%" + Utils.LAZILY);
    }

    private Expression<RegionWrapper> wrappers;
    private Expression<Biome> biome;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        wrappers = (Expression<RegionWrapper>) exprs[0];
        biome = (Expression<Biome>) exprs[1];
        setBlocking(!parseResult.hasTag("lazily"));
        return true;
    }

    @Override
    protected void execute(Event event) {
        Biome preBiome = this.biome.getSingle(event);
        if (preBiome == null)
            return;
        BiomeType biome = BukkitAdapter.adapt(preBiome);

        for (RegionWrapper wrapper : wrappers.getArray(event)) {
            RunnableUtils.run(Runnables.getBiomeRunnable(wrapper, biome), isBlocking());
        }

    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return new BlockingSyntaxStringBuilder(event, debug, isBlocking())
            .append("set the biome of ", wrappers, " to ", biome)
            .toString();
    }
}
