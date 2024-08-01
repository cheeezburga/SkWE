package me.cheezburga.skwe.api.utils.schematics;

import ch.njol.skript.lang.util.SimpleLiteral;
import me.cheezburga.skwe.api.utils.MaskWrapper;
import org.bukkit.Location;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.EntryValidator.EntryValidatorBuilder;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

public class EntryValidators {

    public static EntryValidatorBuilder base() {
        EntryValidatorBuilder builder = EntryValidator.builder();
        builder.addEntryData(new ExpressionEntryData<>("mask", null, true, MaskWrapper.class));
        return builder;
    }

    public static EntryValidatorBuilder save() {
        EntryValidatorBuilder builder = base();
        builder.addEntryData(new ExpressionEntryData<>("origin", null, true, Location.class));
        builder.addEntryData(new ExpressionEntryData<>("copy entities", new SimpleLiteral<>(false, false), true, Boolean.class));
        builder.addEntryData(new ExpressionEntryData<>("copy biomes", new SimpleLiteral<>(false, false), true, Boolean.class));
        builder.addEntryData(new ExpressionEntryData<>("remove entities", new SimpleLiteral<>(false, false), true, Boolean.class));
        builder.addEntryData(new ExpressionEntryData<>("overwrite", new SimpleLiteral<>(true, false), true, Boolean.class));
        return builder;
    }

    public static EntryValidatorBuilder paste() {
        EntryValidatorBuilder builder = base();
        builder.addEntryData(new ExpressionEntryData<>("rotation", new SimpleLiteral<>(0, false), true, Integer.class));
        builder.addEntryData(new ExpressionEntryData<>("paste entities", new SimpleLiteral<>(false, false), true, Boolean.class));
        builder.addEntryData(new ExpressionEntryData<>("paste biomes", new SimpleLiteral<>(false, false), true, Boolean.class));
        builder.addEntryData(new ExpressionEntryData<>("ignore air", new SimpleLiteral<>(false, false), true, Boolean.class));
        return builder;
    }

}
