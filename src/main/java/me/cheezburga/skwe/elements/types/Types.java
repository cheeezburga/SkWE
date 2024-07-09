package me.cheezburga.skwe.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector2;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.ConvexPolyhedralRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import com.sk89q.worldedit.regions.Region;
import me.cheezburga.skwe.api.utils.EnumWrapper;
import me.cheezburga.skwe.api.utils.MaskWrapper;
import me.cheezburga.skwe.api.utils.PatternWrapper;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.Getters;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.StreamCorruptedException;
import java.util.HashSet;
import java.util.Set;

public class Types {
    static {
        EnumWrapper<WorldEditShape> SHAPE_ENUM = new EnumWrapper<>(WorldEditShape.class);
        Classes.registerClass(SHAPE_ENUM.getClassInfo("worldeditshape")
                .user("world ?edit ?shapes?")
                .name("World Edit Shapes")
                .description("All the supported creatable shapes using WorldEdit.")
                .since("1.0.0"));

        Classes.registerClass(new ClassInfo<>(PatternWrapper.class, "worldeditpattern")
                .user("(world ?edit|fawe) ?patterns?")
                .name("World Edit Pattern")
                .description("Represents a pattern that can be used for WorldEdit operations.")
                .since("1.0.0")
                .serializer(new Serializer<>() {
                    @Override
                    public @NotNull Fields serialize(PatternWrapper wrapper) {
                        Fields f = new Fields();
                        f.putObject("pattern", wrapper.asString());
                        return f;
                    }

                    @Override
                    public void deserialize(PatternWrapper wrapper, @NotNull Fields fields) {
                        assert false;
                    }

                    @Override
                    protected PatternWrapper deserialize(@NotNull Fields fields) throws StreamCorruptedException {
                        String asString = fields.getObject("pattern", String.class);
                        return new PatternWrapper(Utils.patternFrom(asString), asString);
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

        Classes.registerClass(new ClassInfo<>(MaskWrapper.class, "worldeditmask")
                .user("(world ?edit|fawe) ?masks?")
                .name("World Edit Mask")
                .description("Represents a mask that can be used for WorldEdit operations.")
                .since("1.0.0")
                .serializer(new Serializer<>() {
                    @Override
                    public @NotNull Fields serialize(MaskWrapper wrapper) {
                        Fields f = new Fields();
                        f.putObject("mask", wrapper.asString());
                        return f;
                    }

                    @Override
                    public void deserialize(MaskWrapper wrapper, @NotNull Fields fields) {
                        assert false;
                    }

                    @Override
                    protected MaskWrapper deserialize(@NotNull Fields fields) throws StreamCorruptedException {
                        String asString = fields.getObject("mask", String.class);
                        return new MaskWrapper(Utils.maskFrom(asString, null), asString);
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

        Classes.registerClass(new ClassInfo<>(RegionWrapper.class, "worldeditregion")
                .user("(world ?edit|fawe) ?regions?")
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
                    public @NotNull Fields serialize(RegionWrapper regionWrapper) {
                        Fields f = new Fields();
                        f.putObject("world", regionWrapper.world());
                        Region region = regionWrapper.region();
                        if (region instanceof CuboidRegion cuboid) {
                            BlockVector3 min = cuboid.getMinimumPoint();
                            BlockVector3 max = cuboid.getMaximumPoint();
                            f.putPrimitive("minX", min.getX());
                            f.putPrimitive("minY", min.getY());
                            f.putPrimitive("minZ", min.getZ());
                            f.putPrimitive("maxX", max.getX());
                            f.putPrimitive("maxY", max.getY());
                            f.putPrimitive("maxZ", max.getZ());
                            f.putObject("region", "cuboid");
                        } else if (region instanceof CylinderRegion cyl) {
                            int minY = cyl.getMinimumY();
                            int maxY = cyl.getMaximumY();
                            BlockVector3 center = cyl.getCenter().subtract(0, ((double) (minY + maxY) / 2), 0).toBlockPoint();
                            Vector2 radius = cyl.getRadius();
                            f.putPrimitive("minY", minY);
                            f.putPrimitive("maxY", maxY);
                            f.putPrimitive("centerX", center.getX());
                            f.putPrimitive("centerY", center.getY());
                            f.putPrimitive("centerZ", center.getZ());
                            f.putPrimitive("radiusX", radius.getX());
                            f.putPrimitive("radiusZ", radius.getZ());
                            f.putObject("region", "cyl");
                        } else if (region instanceof EllipsoidRegion ellipsoid) {
                            Vector3 center = ellipsoid.getCenter();
                            Vector3 radius = ellipsoid.getRadius();
                            f.putPrimitive("centerX", center.getX());
                            f.putPrimitive("centerY", center.getY());
                            f.putPrimitive("centerZ", center.getZ());
                            f.putPrimitive("radiusX", radius.getX());
                            f.putPrimitive("radiusY", radius.getY());
                            f.putPrimitive("radiusZ", radius.getZ());
                            f.putObject("region", "ellipsoid");
                        } else if (region instanceof ConvexPolyhedralRegion convex) {
                            BlockVector3[] vertices = convex.getVertices().toArray(new BlockVector3[0]);
                            f.putPrimitive("size", vertices.length);
                            for (int i = 0; i < vertices.length; i++) {
                                f.putPrimitive(String.format("vertex-%dX", i), vertices[i].getX());
                                f.putPrimitive(String.format("vertex-%dY", i), vertices[i].getY());
                                f.putPrimitive(String.format("vertex-%dZ", i), vertices[i].getZ());
                            }
                            f.putObject("region", "convex");
                        }
                        return f;
                    }

                    @Override
                    public void deserialize(RegionWrapper regionWrapper, @NotNull Fields fields) {
                        assert false;
                    }

                    @Override
                    protected RegionWrapper deserialize(@NotNull Fields f) throws StreamCorruptedException {
                        String regionType = f.getObject("region", String.class);
                        assert regionType != null;
                        Region region = new CuboidRegion(BlockVector3.ZERO, BlockVector3.ZERO);
                        World world = f.getObject("world", World.class);
                        switch (regionType) {
                            case "cuboid" -> {
                                BlockVector3 min = BlockVector3.at(f.getPrimitive("minX", int.class), f.getPrimitive("minY", int.class), f.getPrimitive("minZ", int.class));
                                BlockVector3 max = BlockVector3.at(f.getPrimitive("maxX", int.class), f.getPrimitive("maxY", int.class), f.getPrimitive("maxZ", int.class));
                                region = Getters.getCuboidRegion(Utils.locationFrom(min, world), Utils.locationFrom(max, world), world);
                            }
                            case "cyl" -> {
                                int minY = f.getPrimitive("minY", int.class);
                                int maxY = f.getPrimitive("maxY", int.class);
                                BlockVector3 center = BlockVector3.at(f.getPrimitive("centerX", int.class), f.getPrimitive("centerY", int.class), f.getPrimitive("centerZ", int.class));
                                Vector2 radius = Vector2.at(f.getPrimitive("radiusX", double.class), f.getPrimitive("radiusZ", double.class));
                                region = Getters.getCylinderRegion(Utils.locationFrom(center, world), radius.getX(), radius.getZ(), (maxY - minY));
                            }
                            case "ellipsoid" -> {
                                BlockVector3 center = BlockVector3.at(f.getPrimitive("centerX", double.class), f.getPrimitive("centerY", double.class), f.getPrimitive("centerZ", double.class));
                                Vector3 radius = Vector3.at(f.getPrimitive("radiusX", double.class), f.getPrimitive("radiusY", double.class), f.getPrimitive("radiusZ", double.class));
                                region = Getters.getEllipsoidRegion(Utils.locationFrom(center, world), radius.getX(), radius.getY(), radius.getZ());
                            }
                            case "convex" -> {
                                int size = f.getPrimitive("size", int.class);
                                Set<Location> vertices = new HashSet<>();
                                for (int i = 0; i < size; i++) {
                                    int x = f.getPrimitive(String.format("vertex-%dX", i), int.class);
                                    int y = f.getPrimitive(String.format("vertex-%dY", i), int.class);
                                    int z = f.getPrimitive(String.format("vertex-%dZ", i), int.class);
                                    vertices.add(new Location(world, x, y, z));
                                }
                                Location[] v = vertices.toArray(new Location[0]);
                                region = Getters.getConvexPolyRegion(v);
                            }
                        }
                        return new RegionWrapper(region, world);
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
