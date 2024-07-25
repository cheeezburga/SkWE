package me.cheezburga.skwe.elements.effects.schematics;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.schematics.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffSaveSchematic extends SkWEEffect {

    static {
        Skript.registerEffect(EffSaveSchematic.class, "save %worldeditregion% as [schem[atic]] %string% [center:with (cent(re|er)|origin) (at|of) %-location%] [overwrite:and overwrite existing]");
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<String> name;
    private Expression<Location> center;
    private boolean overwrite;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (WorldEdit.getInstance().getPlatformManager().queryCapability(Capability.GAME_HOOKS).getDataVersion() == -1) {
            Utils.log("&cSchematics are not supported on this server!");
            return false;
        }
        wrapper = (Expression<RegionWrapper>) exprs[0];
        name = (Expression<String>) exprs[1];
        center = (Expression<Location>) exprs[2];
        overwrite = parseResult.hasTag("overwrite");
        return true;
    }

    @Override
    protected void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null)
            return;

        String name = this.name.getSingle(event);
        if (name == null)
            return;

        Location center = null;
        if (this.center != null) {
            center = this.center.getSingle(event);
            if (center == null)
                return;
        }

        ClipboardFormat format = BuiltInClipboardFormat.SPONGE_SCHEMATIC;

        RunnableUtils.run(Runnables.getSaveRunnable(wrapper, format, name, center, this.overwrite));
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "save " + wrapper.toString(event, debug) + " to .schem file " + this.name.toString(event, debug);
    }
}
