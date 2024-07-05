package me.cheezburga.skwe.api.utils.shape;

import ch.njol.skript.lang.util.SimpleLiteral;
import com.sk89q.worldedit.function.pattern.Pattern;
import me.cheezburga.skwe.elements.types.WorldEditShape;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.EntryValidator.EntryValidatorBuilder;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

public class EntryValidators {

    //TODO: make this a static map and get the respective value instead of creating one every time
    public static EntryValidatorBuilder get(WorldEditShape shape) {
        switch (shape) {
            case ELLIPSE, ELLIPSOID, SPHERE -> { return sphere(); }
            case CYLINDER -> { return cylinder(); }
            case PYRAMID -> { return pyramid(); }
            default -> { return base(); }
        }
    }

    public static EntryValidatorBuilder base() {
        EntryValidatorBuilder builder = EntryValidator.builder();
        builder.addEntryData(new ExpressionEntryData<>("pattern", null, false, Pattern.class));
        builder.addEntryData(new ExpressionEntryData<>("hollow", new SimpleLiteral<>(true, false), true, Boolean.class));
        return builder;
    }

    public static EntryValidatorBuilder sphere() {
        EntryValidatorBuilder builder = base();
        builder.addEntryData(new ExpressionEntryData<>("radius", null, false, Number.class));
        builder.addEntryData(new ExpressionEntryData<>("radiusY", null, true, Number.class)); // default will just be the radius key
        builder.addEntryData(new ExpressionEntryData<>("radiusZ", null, true, Number.class)); // default will just be the radius key
        return builder;
    }

    public static EntryValidatorBuilder cylinder() {
        EntryValidatorBuilder builder = base();
        builder.addEntryData(new ExpressionEntryData<>("radius", null, false, Number.class));
        builder.addEntryData(new ExpressionEntryData<>("radiusZ", null, true, Number.class)); // default will just be the radius key
        builder.addEntryData(new ExpressionEntryData<>("height", null, false, Number.class));
        return builder;
    }

    public static EntryValidatorBuilder pyramid() {
        EntryValidatorBuilder builder = base();
        builder.addEntryData(new ExpressionEntryData<>("size", null, false, Number.class));
        return builder;
    }
}
