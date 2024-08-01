package me.cheezburga.skwe.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import me.cheezburga.skwe.api.utils.MaskWrapper;
import me.cheezburga.skwe.api.utils.RunnableUtils;
import me.cheezburga.skwe.api.utils.schematics.EntryValidators;
import me.cheezburga.skwe.api.utils.schematics.Runnables;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;

import java.util.List;

public class SecPasteSchematic extends Section {

    static {
        Skript.registerSection(SecPasteSchematic.class, "paste [schem[atic] (named|with name)] %string% at %locations%");
    }

    private Expression<String> name;
    private Expression<Location> locations;
    private Expression<Number> rotation;
    private Expression<MaskWrapper> preMask;
    private Expression<Boolean> pasteEntities, pasteBiomes, ignoreAir;

    @SuppressWarnings({"unchecked", "NullableProblems"})
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

        Object preMask = this.preMask == null ? null : this.preMask.getSingle(event);
        int rotation = this.rotation == null ? 0 : this.rotation.getOptionalSingle(event).orElse(0).intValue();

        boolean pasteEntities = Boolean.TRUE.equals(this.pasteEntities.getSingle(event));
        boolean pasteBiomes = Boolean.TRUE.equals(this.pasteBiomes.getSingle(event));
        boolean ignoreAir = Boolean.TRUE.equals(this.ignoreAir.getSingle(event));

        for (Location location : this.locations.getArray(event)) {
            RunnableUtils.run(Runnables.getPasteRunnable(name, location, rotation, preMask, ignoreAir, pasteEntities, pasteBiomes));
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        String builder = "paste schematic named " +
                this.name.toString(event, debug) +
                " at " + this.locations.toString(event, debug) +
                (this.preMask == null ? " with no mask " : " with mask of " + this.preMask.toString(event, debug)) +
                (this.rotation == null ? " with rotation of 0 " : " with rotation of " + this.rotation.toString(event, debug)) +
                (Boolean.TRUE.equals(this.pasteEntities.getSingle(event)) ? " while pasting entities" : " while not pasting entities") +
                (Boolean.TRUE.equals(this.pasteBiomes.getSingle(event)) ? " while pasting biomes" : " while not pasting biomes") +
                (Boolean.TRUE.equals(this.ignoreAir.getSingle(event)) ? " while ignoring air" : " while not ignoring air");
        return builder;
    }

}
