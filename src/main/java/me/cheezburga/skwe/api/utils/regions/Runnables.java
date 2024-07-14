package me.cheezburga.skwe.api.utils.regions;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.GroundFunction;
import com.sk89q.worldedit.function.generator.FloraGenerator;
import com.sk89q.worldedit.function.mask.*;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.visitor.LayerVisitor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.convolution.GaussianKernel;
import com.sk89q.worldedit.math.convolution.HeightMap;
import com.sk89q.worldedit.math.convolution.HeightMapFilter;
import com.sk89q.worldedit.math.noise.RandomNoise;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.Regions;
import com.sk89q.worldedit.world.RegenOptions;
import me.cheezburga.skwe.SkWE;
import me.cheezburga.skwe.api.utils.Utils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Runnables {

    public static Runnable getSmoothRunnable(RegionWrapper wrapper, int iterations, Mask mask) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(wrapper.world()))) {
                HeightMap map = new HeightMap(session, wrapper.region(), mask);
                HeightMapFilter filter = new HeightMapFilter(new GaussianKernel(5, 1.0));
                map.applyFilter(filter, iterations);
            } catch (MaxChangedBlocksException ignored) {}
        };
    }

    public static Runnable getWallsRunnable(RegionWrapper wrapper, Pattern pattern) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(wrapper.world()))) {
                session.makeWalls(wrapper.region(), pattern);
            } catch (MaxChangedBlocksException ignored) {}
        };
    }

    public static Runnable getFacesRunnable(RegionWrapper wrapper, Pattern pattern) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(wrapper.world()))) {
                session.makeFaces(wrapper.region(), pattern);
            } catch (MaxChangedBlocksException ignored) {}
        };
    }

    public static Runnable getOverlayRunnable(RegionWrapper wrapper, Pattern pattern) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(wrapper.world()))) {
                session.overlayCuboidBlocks(wrapper.region(), pattern);
            } catch (MaxChangedBlocksException ignored) {}
        };
    }

    public static Runnable getCenterRunnable(RegionWrapper wrapper, Pattern pattern) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(wrapper.world()))) {
                session.center(wrapper.region(), pattern);
            } catch (MaxChangedBlocksException ignored) {}
        };
    }

    public static Runnable getMoveRunnable(RegionWrapper wrapper, @Nullable Pattern pattern, @Nullable Object preMask, BlockVector3 direction, int distance, boolean ignoreAir, boolean copyEntities, boolean copyBiomes) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(wrapper.world()))) {
                Mask mask = Utils.maskFrom(preMask, Utils.contextFrom(session, wrapper.world()));
                Mask finalMask;
                if (ignoreAir) {
                    if (mask == null) {
                        finalMask = new ExistingBlockMask(session);
                    } else {
                        finalMask = new MaskIntersection(mask, new ExistingBlockMask(session));
                    }
                } else {
                    finalMask = mask;
                }
                session.moveRegion(wrapper.region(), direction, distance, copyEntities, copyBiomes, finalMask, pattern != null ? pattern : Utils.AIR_PATTERN);
            } catch (MaxChangedBlocksException ignored) {}
        };
    }

    public static Runnable getRegenRunnable(RegionWrapper wrapper, @Nullable Long seed, boolean regenBiomes) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(wrapper.world()))) {
                RegenOptions options = RegenOptions.builder().seed(seed != null ? seed : wrapper.world().getSeed()).regenBiomes(regenBiomes).build();
                BukkitAdapter.adapt(wrapper.world()).regenerate(wrapper.region(), session, options);
            }
        };
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    public static Runnable getHollowRunnable(RegionWrapper wrapper, Pattern pattern, @Nullable Mask mask, int thickness) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(wrapper.world()))) {
                if (SkWE.HAS_FAWE) {
                    try {
                        Method method = EditSession.class.getMethod("hollowOutRegion", Region.class, int.class, Pattern.class, Mask.class);
                        Mask finalMask;
                        if (mask != null) {
                            Class<?> traverserClass = Class.forName("com.fastasyncworldedit.core.util.MaskTraverser");
                            Constructor<?> constructor = traverserClass.getConstructor(Mask.class);
                            Object traverser = constructor.newInstance(mask);
                            Method setExtentMethod = traverserClass.getMethod("setNewExtent", Extent.class);
                            setExtentMethod.invoke(traverser, session);
                            finalMask = mask;
                        } else {
                            finalMask = new SolidBlockMask(session);
                        }
                        method.invoke(session, wrapper.region(), thickness, pattern, finalMask);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | InstantiationException ignored) {}
                } else {
                    session.hollowOutRegion(wrapper.region(), thickness, pattern);
                }
            } catch (MaxChangedBlocksException ignored) {}
        };
    }

    public static Runnable getFloraRunnable(RegionWrapper wrapper, double density) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(wrapper.world()))) {
                double d = Math.clamp(density, 0, 100);
                d /= 100;
                FloraGenerator generator = new FloraGenerator(session);
                GroundFunction ground = new GroundFunction(new ExistingBlockMask(session), generator);
                LayerVisitor visitor = new LayerVisitor(Regions.asFlatRegion(wrapper.region()), Regions.minimumBlockY(wrapper.region()), Regions.maximumBlockY(wrapper.region()), ground);
                visitor.setMask(new NoiseFilter2D(new RandomNoise(), d));
                Operations.completeLegacy(visitor);
            } catch (MaxChangedBlocksException ignored) {}
        };
    }

    public static Runnable getNaturalizeRunnable(RegionWrapper wrapper) {
        return () -> {
            try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(wrapper.world()))) {
                session.naturalizeCuboidBlocks(wrapper.region());
            } catch (MaxChangedBlocksException ignored) {}
        };
    }

}
