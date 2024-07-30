package me.cheezburga.skwe.api.utils.schematics;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Runnables {

    public static Runnable getSaveRunnable(RegionWrapper wrapper, String name, @Nullable Location centre, boolean shouldOverwrite) {
        ClipboardFormat format = BuiltInClipboardFormat.SPONGE_SCHEMATIC;

        Path directory = Paths.get(WorldEdit.getInstance().getConfiguration().getWorkingDirectoryPath().toString(), "schematics");
        Path filePath = directory.resolve(name + "." + format.getPrimaryFileExtension());

        if (Files.exists(filePath)) {
            if (!shouldOverwrite) {
                Utils.log("&cA schematic with name " + name + " already exists!");
                return () -> {};
            } else {
                try {
                    Files.delete(filePath);
                } catch (IOException e) {
                    Utils.log("&cRan into a problem overwriting the existing schematic: " + e.getMessage());
                    return () -> {};
                }
            }
        }

        try {
            Files.createDirectories(directory);
            Files.createFile(filePath);
        } catch (IOException e) {
            Utils.log("&cRan into a problem creating the schematic file: " + e.getMessage());
            return () -> {};
        }

        return () -> {
            try (ClipboardWriter writer = format.getWriter(new FileOutputStream(filePath.toFile()))) {
                Clipboard clipboard = new BlockArrayClipboard(wrapper.region());
                if (centre != null)
                    clipboard.setOrigin(Utils.blockVector3From(centre));

                World world = BukkitAdapter.adapt(wrapper.world());
                ForwardExtentCopy copy = new ForwardExtentCopy(world, wrapper.region(), clipboard, wrapper.region().getMinimumPoint());
                Operations.complete(copy);

                writer.write(clipboard);
            } catch (IOException | WorldEditException e) {
                Utils.log("&cRan into a problem writing the region to the schematic file: " + e.getMessage());
            }
        };
    }

    public static Runnable getPasteRunnable(String name, Location location, int rotation, boolean ignoreAir) {
        ClipboardFormat format = BuiltInClipboardFormat.SPONGE_SCHEMATIC;

        Path directory = Paths.get(WorldEdit.getInstance().getConfiguration().getWorkingDirectoryPath().toString(), "schematics");
        Path filePath = directory.resolve(name + "." + format.getPrimaryFileExtension());

        if (!Files.exists(filePath)) {
            Utils.log("&cCouldn't find a schematic named " + name);
            return () -> {};
        }

        File file = filePath.toFile();

        // format defaults to sponge with skript-worldedit, so no need for this
        // ClipboardFormat format = ClipboardFormats.findByFile(file);
        // if (format == null) {
        //     Utils.log("&cCouldn't get the format of the schematic named " + name);
        //     return () -> {};
        // }

        return () -> {
            try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                Clipboard clipboard = reader.read();
                try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(location.getWorld()))) {
                    ClipboardHolder holder = new ClipboardHolder(clipboard);
                    if (rotation != 0)
                        holder.setTransform(new AffineTransform().rotateY(rotation));
                    Operation operation = holder
                            .createPaste(session)
                            .to(Utils.blockVector3From(location))
                            .ignoreAirBlocks(ignoreAir)
                            .build();
                    Operations.completeLegacy(operation);
                }
            } catch (IOException | WorldEditException e) {
                Utils.log("&cRan into a problem pasting the schematic named " + name);
            }
        };
    }





}
