package me.cheezburga.skwe.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import com.sk89q.worldedit.regions.Region;
import me.cheezburga.skwe.api.utils.EnumWrapper;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

public class Types {
    static {
        EnumWrapper<WorldEditShape> SHAPE_ENUM = new EnumWrapper<>(WorldEditShape.class);
        Classes.registerClass(SHAPE_ENUM.getClassInfo("worldeditshape")
                .user("world ?edit ?shapes?")
                .name("World Edit Shapes")
                .description("All the supported creatable shapes using WorldEdit.")
                .since("1.0.0"));

        Classes.registerClass(new ClassInfo<>(RegionWrapper.class, "worldeditregion")
                .user("(world ?edit|fawe) region")
                .name("World Edit Region")
                .description("Represents a region that can be used for WorldEdit operations.")
                .since("1.0.0")
                .parser(new Parser<>() {
                    @Override
                    public RegionWrapper parse(@NotNull String s, @NotNull ParseContext context) {
                        return null;
                    }

                    @Override
                    public boolean canParse(@NotNull ParseContext context) {
                        return false;
                    }

                    @Override
                    public @NotNull String toString(RegionWrapper regionWrapper, int flags) {
                        return regionWrapper.toString();
                    }

                    @Override
                    public @NotNull String toVariableNameString(RegionWrapper regionWrapper) {
                        return regionWrapper.toString();
                    }
                })
                .serializer(new Serializer<>() {
                    @Override
                    public @NotNull Fields serialize(RegionWrapper regionWrapper) throws NotSerializableException {
                        Fields f = new Fields();
                        f.putObject("region", regionWrapper.getRegion());
                        f.putObject("world", regionWrapper.getWorld());
                        return f;
                    }

                    @Override
                    public void deserialize(RegionWrapper regionWrapper, @NotNull Fields fields) {
                        assert false;
                    }

                    @Override
                    protected RegionWrapper deserialize(@NotNull Fields fields) throws StreamCorruptedException {
                        return new RegionWrapper(
                                fields.getObject("region", Region.class),
                                fields.getObject("world", World.class)
                        );
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    protected boolean canBeInstantiated() {
                        return false;
                    }
                }));
    }
}
