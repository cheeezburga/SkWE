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
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import me.cheezburga.skwe.api.utils.schematics.EntryValidators;
import me.cheezburga.skwe.api.utils.schematics.Runnables;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator.EntryValidatorBuilder;

import java.util.List;

public class SecSaveSchematic extends Section {

    static {
        Skript.registerSection(SecSaveSchematic.class, "save %worldeditregion% as [a] [schem[atic] (named|with name)] %string%");
    }

    private Expression<RegionWrapper> wrapper;
    private Expression<String> name;
    private Expression<MaskWrapper> preMask;
    private Expression<Boolean> copyEntities, copyBiomes, removeEntities, overwrite;
    private Expression<Location> origin;

    @SuppressWarnings({"unchecked", "NullableProblems"})
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
        String name = this.name.getSingle(event);
        RegionWrapper wrapper = this.wrapper.getSingle(event);

        Object preMask = this.preMask == null ? null : this.preMask.getSingle(event);
        Location origin = this.origin == null ? null : this.origin.getSingle(event);

        boolean copyEntities = Boolean.TRUE.equals(this.copyEntities.getSingle(event));
        boolean copyBiomes = Boolean.TRUE.equals(this.copyBiomes.getSingle(event));
        boolean removeEntities = Boolean.TRUE.equals(this.removeEntities.getSingle(event));
        boolean overwrite = Boolean.TRUE.equals(this.overwrite.getSingle(event));

        RunnableUtils.run(Runnables.getSaveRunnable(wrapper, name, origin, preMask, overwrite, copyEntities, copyBiomes, removeEntities));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString(@Nullable Event event, boolean debug) {
        String builder = "save " +
                this.wrapper.toString(event, debug) +
                " as a schematic named " +
                this.name.toString(event, debug) +
                (this.origin == null ? " with default origin " : " with origin at " + this.origin.toString(event, debug)) +
                (this.preMask == null ? " with no mask " : " with mask of " + this.preMask.toString(event, debug)) +
                (Boolean.TRUE.equals(this.overwrite.getSingle(event)) ? " while overwriting" : " while not overwriting") +
                (Boolean.TRUE.equals(this.copyEntities.getSingle(event)) ? " while copying entities" : " while not copying entities") +
                (Boolean.TRUE.equals(this.copyBiomes.getSingle(event)) ? " while copying biomes" : " while not copying biomes") +
                (Boolean.TRUE.equals(this.removeEntities.getSingle(event)) ? " while removing entities" : " while not removing entities");
        return builder;
    }

}
