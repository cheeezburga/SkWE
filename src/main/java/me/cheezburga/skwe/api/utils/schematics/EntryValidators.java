package me.cheezburga.skwe.api.utils.schematics;

import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.util.SimpleLiteral;
import me.cheezburga.skwe.api.utils.MaskWrapper;
import org.bukkit.Location;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.EntryValidator.EntryValidatorBuilder;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

public class EntryValidators {

    public static EntryValidatorBuilder base() {
        EntryValidatorBuilder builder = EntryValidator.builder();
        builder.addEntryData(new ExpressionEntryData<>("name", null, false, String.class));
        builder.addEntryData(new ExpressionEntryData<>("copy entities", new SimpleLiteral<>(false, false), true, Boolean.class));
        builder.addEntryData(new ExpressionEntryData<>("copy biomes", new SimpleLiteral<>(false, false), true, Boolean.class));
        builder.addEntryData(new ExpressionEntryData<>("mask", null, true, MaskWrapper.class));
        return builder;
    }

    public static EntryValidatorBuilder save() {
        EntryValidatorBuilder builder = base();
        builder.addEntryData(new ExpressionEntryData<>("center", null, true, Location.class));
        builder.addEntryData(new ExpressionEntryData<>("remove entities", new SimpleLiteral<>(false, false), true, Boolean.class));
        builder.addEntryData(new ExpressionEntryData<>("overwrite", new SimpleLiteral<>(true, false), true, Boolean.class));
        return builder;
    }

    public static EntryValidatorBuilder paste() {
        EntryValidatorBuilder builder = base();
        builder.addEntryData(new ExpressionEntryData<>("rotation", new SimpleLiteral<>(0, false), true, Integer.class));
        builder.addEntryData(new ExpressionEntryData<>("set region to", null, true, Variable.class));
        return builder;
    }

}
