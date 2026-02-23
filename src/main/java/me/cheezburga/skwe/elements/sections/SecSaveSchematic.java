package me.cheezburga.skwe.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import java.util.List;
import me.cheezburga.skwe.api.utils.MaskWrapper;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.schematics.EntryValidators;
import me.cheezburga.skwe.api.utils.schematics.Runnables;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator.EntryValidatorBuilder;

@Name("Schematic - Save")
@Description({
    "Saves a region as a schematic, with a bunch of configurable options (see below for a list of them).",
    "Note that the name of the schematic shouldn't include any path, as skript-worldedit will always start at WorldEdit's schematics directory.",
    "",
    "Entries:",
    "* copy entities: whether the schematic should copy entities",
    "* copy biomes: whether the schematic should copy biomes",
    "* remove entities: whether the schematic should remove entities",
    "* overwrite: whether the schematic should overwrite any schematics of the same name (if false, the schematic won't save if a schematic is found with the same name)",
    "* origin: where the origin of the saved schematic should be",
    "* mask: a mask which will only let matching blocks be copied"
})
@Examples({
    "save {region} as a schematic named \"example\":",
        "\tcopy entities: true",
        "\tcopy biomes: true",
        "\tmask: mask from \"stone,red_wool,blue_wool\"",
        "\torigin: location of (first element out of all players)",
        "\tremove entities: true",
        "\toverwrite: true"
})
@Since("1.1.0")
@RequiredPlugins("WorldEdit")
public class SecSaveSchematic extends Section {

    static {
        Skript.registerSection(SecSaveSchematic.class, "save %worldeditregion% as [a] [schem[atic] (named|with name)] %string%");
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<String> name;
    private Expression<MaskWrapper> preMask;
    private Expression<Boolean> copyEntities, copyBiomes, removeEntities, overwrite;
    private Expression<Location> origin;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
        EntryValidatorBuilder builder = EntryValidators.save();
        EntryContainer container = builder.build().validate(sectionNode);
        if (container == null) return false;

        wrapper = (Expression<RegionWrapper>) exprs[0];
        name = (Expression<String>) exprs[1];
        preMask = (Expression<MaskWrapper>) container.getOptional("mask", true);
        origin = (Expression<Location>) container.getOptional("origin", true);
        copyEntities = (Expression<Boolean>) container.get("copy entities", true);
        copyBiomes = (Expression<Boolean>) container.get("copy biomes", true);
        removeEntities = (Expression<Boolean>) container.get("remove entities", true);
        overwrite = (Expression<Boolean>) container.get("overwrite", true);

        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        execute(event);
        return super.walk(event, false);
    }

    private void execute(Event event) {
        RegionWrapper wrapper = this.wrapper.getSingle(event);
        if (wrapper == null) {
            error("The provided region was not set!", Utils.toHighlight(this.wrapper));
            return;
        }

        String name = this.name.getSingle(event);
        if (name == null) {
            error("The provided name was not set!", Utils.toHighlight(this.name));
            return;
        }

        Object preMask = this.preMask == null ? null : this.preMask.getSingle(event);
        Location origin = this.origin == null ? null : this.origin.getSingle(event);

        boolean copyEntities = Boolean.TRUE.equals(this.copyEntities.getSingle(event));
        boolean copyBiomes = Boolean.TRUE.equals(this.copyBiomes.getSingle(event));
        boolean removeEntities = Boolean.TRUE.equals(this.removeEntities.getSingle(event));
        boolean overwrite = Boolean.TRUE.equals(this.overwrite.getSingle(event));

        RunnableUtils.run(Runnables.getSaveRunnable(wrapper, name, origin, preMask, overwrite, copyEntities, copyBiomes, removeEntities));
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return new SyntaxStringBuilder(event, debug)
            .append("save ", wrapper, " as a schematic named " + name)
            .append(this.origin == null ? " with default origin " : " with origin at " + origin)
            .append(this.preMask == null ? " with no mask " : " with mask of " + preMask)
            .append(Boolean.TRUE.equals(this.overwrite.getSingle(event)) ? " while overwriting" : " while not overwriting")
            .append(Boolean.TRUE.equals(this.copyEntities.getSingle(event)) ? " while copying entities" : " while not copying entities")
            .append(Boolean.TRUE.equals(this.copyBiomes.getSingle(event)) ? " while copying biomes" : " while not copying biomes")
            .append(Boolean.TRUE.equals(this.removeEntities.getSingle(event)) ? " while removing entities" : " while not removing entities")
            .toString();
    }

}
