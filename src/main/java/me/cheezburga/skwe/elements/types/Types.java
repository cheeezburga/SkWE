package me.cheezburga.skwe.elements.types;

import ch.njol.skript.registrations.Classes;
import me.cheezburga.skwe.api.utils.EnumWrapper;

public class Types {
    static {
        EnumWrapper<WorldEditShape> SHAPE_ENUM = new EnumWrapper<>(WorldEditShape.class);
        Classes.registerClass(SHAPE_ENUM.getClassInfo("worldeditshape")
                .user("world ?edit ?shapes?")
                .name("World Edit Shapes")
                .description("All the supported creatable shapes using WorldEdit.")
                .since("1.0.0"));
    }
}
