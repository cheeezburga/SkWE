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
import me.cheezburga.skwe.api.utils.schematics.EntryValidators;
import me.cheezburga.skwe.api.utils.schematics.Runnables;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;

@Name("Schematic - Paste")
@Description({
    "Pastes a schematic, with a bunch of configurable options (see below for a list of them).",
    "Note that the name of the schematic shouldn't include any path, as skript-worldedit will always start at WorldEdit's schematics directory.",
    "",
    "Entries:",
    "* paste entities: whether the schematic should copy entities",
    "* paste biomes: whether the schematic should copy biomes",
    "* ignore air: whether the schematic should paste air blocks",
    "* rotation: the number of degrees that the pasted schematic should be rotated by (around the y-axis)",
    "* mask: a mask which will only let matching blocks be pasted"
})
@Examples({
    "paste schematic named \"example\" at {location}:",
    "\tpaste entities: true",
    "\tpaste biomes: true",
    "\tmask: mask from \"stone,red_wool,blue_wool\"",
    "\trotation: 45",
    "\tignore air: true"
})
@Since("1.1.0")
@RequiredPlugins("WorldEdit")
public class SecPasteSchematic extends Section {

    static {
        Skript.registerSection(SecPasteSchematic.class, "paste [schem[atic] (named|with name)] %string% at %locations%");
    }

    private Expression<String> name;
    private Expression<Location> locations;
    private Expression<Number> rotation;
    private Expression<MaskWrapper> preMask;
    private Expression<Boolean> pasteEntities, pasteBiomes, ignoreAir;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
        EntryValidator.EntryValidatorBuilder builder = EntryValidators.paste();
        EntryContainer container = builder.build().validate(sectionNode);
        if (container == null) return false;

        name = (Expression<String>) exprs[0];
        locations = (Expression<Location>) exprs[1];
        rotation = (Expression<Number>) container.getOptional("rotation", true);
        preMask = (Expression<MaskWrapper>) container.getOptional("mask", true);
        pasteEntities = (Expression<Boolean>) container.get("paste entities", true);
        pasteBiomes = (Expression<Boolean>) container.get("paste biomes", true);
        ignoreAir = (Expression<Boolean>) container.get("ignore air", true);

        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        execute(event);
        return super.walk(event, false);
    }

    private void execute(Event event) {
        String name = this.name.getSingle(event);
        if (name == null) {
            error("The provided name was not set!", Utils.toHighlight(this.name));
            return;
        }

        Location[] locations = this.locations.getArray(event);
        if (locations.length < 1) {
            warning("No location(s) was provided!", Utils.toHighlight(this.locations));
            return;
        }

        Object preMask = this.preMask == null ? null : this.preMask.getSingle(event);
        int rotation = this.rotation == null ? 0 : this.rotation.getOptionalSingle(event).orElse(0).intValue();

        boolean pasteEntities = Boolean.TRUE.equals(this.pasteEntities.getSingle(event));
        boolean pasteBiomes = Boolean.TRUE.equals(this.pasteBiomes.getSingle(event));
        boolean ignoreAir = Boolean.TRUE.equals(this.ignoreAir.getSingle(event));

        for (Location location : locations) {
            RunnableUtils.run(Runnables.getPasteRunnable(name, location, rotation, preMask, ignoreAir, pasteEntities, pasteBiomes));
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return new SyntaxStringBuilder(event, debug)
            .append("paste schematic named ", name, " at ", locations)
            .append(" with ", this.preMask == null ? "no " : preMask, "mask ")
            .append(" with rotation of ", this.rotation == null ? "0 " : rotation)
            .append(Boolean.TRUE.equals(this.pasteEntities.getSingle(event)) ? " while pasting entities" : " while not pasting entities")
            .append(Boolean.TRUE.equals(this.pasteBiomes.getSingle(event)) ? " while pasting biomes" : " while not pasting biomes")
            .append(Boolean.TRUE.equals(this.ignoreAir.getSingle(event)) ? " while ignoring air" : " while not ignoring air")
            .toString();
    }

}
