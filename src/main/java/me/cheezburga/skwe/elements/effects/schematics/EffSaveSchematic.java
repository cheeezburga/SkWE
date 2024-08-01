package me.cheezburga.skwe.elements.effects.schematics;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.Capability;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.schematics.Runnables;
import me.cheezburga.skwe.lang.SkWEEffect;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Schematic - Save")
@Description({
        "Saves a region as a schematic.",
        "The name of the schematic shouldn't include any path, as skript-worldedit will always start at WorldEdit's schematics directory.",
        "The centre of the schematic can be set, as well as whether or not it should overwrite an existing schematic of the same name."
})
@Examples("save {region} as a schematic named \"example_schematic\" and overwrite existing")
@Since("1.1.0")
@RequiredPlugins("WorldEdit")
public class EffSaveSchematic extends SkWEEffect {

    static {
        Skript.registerEffect(EffSaveSchematic.class, "save %worldeditregion% as [a] [schem[atic] (named|with name)] %string% [overwrite:[and] overwrit(e|ing) existing]");
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
    @SuppressWarnings("NullableProblems")
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

        RunnableUtils.run(Runnables.getSaveRunnable(wrapper, name, center, null, this.overwrite, false, false, false));
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public String toString(@Nullable Event event, boolean debug) {
        return "save " + wrapper.toString(event, debug) + " as a schematic named \"" + this.name.toString(event, debug) + "\" with origin at " + (this.center != null ? this.center.toString(event, debug) : "default") + " and" + (this.overwrite ? "" : " not") + " overwriting the existing file";
    }
}
