package me.cheezburga.skwe.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import me.cheezburga.skwe.SkWE;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class EffPasteSchematic extends Effect {

    static {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit"))
            Skript.registerEffect(EffSet.class,
                    "paste %string% at %locations% [as %-player%]");
    }

    private Expression<String> schematic;
    private Expression<Location> loc;
    private Expression<Player> player;

    @Override
    @SuppressWarnings({"NullableProblems","unchecked"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.schematic = (Expression<String>) exprs[0];
        this.loc = (Expression<Location>) exprs[1];
        this.player = (Expression<Player>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event event) {
        return;
    }

    @Override
    @NotNull
    public String toString(@Nullable Event event, boolean debug) {
        return "paste " + schematic.toString(event, debug) + " at " + loc.toString(event, debug) + (player != null ? " as " + player.toString(event, debug) : "");
    }
}
