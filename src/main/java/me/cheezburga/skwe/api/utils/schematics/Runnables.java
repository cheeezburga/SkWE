package me.cheezburga.skwe.api.utils.schematics;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
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

import static me.cheezburga.skwe.api.utils.schematics.Utils.findSchematicFile;

public class Runnables {

    public static Runnable getSaveRunnable(RegionWrapper wrapper, String name, @Nullable Location centre, @Nullable Object preMask, boolean shouldOverwrite, boolean copyEntities, boolean copyBiomes, boolean removeEntities) {
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
            try (
				ClipboardWriter writer = format.getWriter(new FileOutputStream(filePath.toFile()));
				EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(wrapper.world()))
            ) {
                Clipboard clipboard = new BlockArrayClipboard(wrapper.region());
                if (centre != null)
                    clipboard.setOrigin(Utils.toBlockVector3(centre));

                ForwardExtentCopy copy = new ForwardExtentCopy(session, wrapper.region(), clipboard, wrapper.region().getMinimumPoint());
				Mask mask = Utils.maskFrom(preMask, null);
				if (mask != null)
					copy.setSourceMask(mask);
                copy.setCopyingEntities(copyEntities);
                copy.setCopyingBiomes(copyBiomes);
                copy.setRemovingEntities(removeEntities);

                Operations.complete(copy);

                writer.write(clipboard);
            } catch (IOException | WorldEditException e) {
                Utils.log("&cRan into a problem writing the region to the schematic file: " + e.getMessage());
            }
        };
    }

    public static Runnable getPasteRunnable(String name, Location location, int rotation, @Nullable Object preMask, boolean ignoreAir, boolean pasteEntities, boolean pasteBiomes) {
        File schematicFile = findSchematicFile(name);
        if (schematicFile == null) {
            Utils.log("&cCouldn't find a schematic using " + name);
            return () -> {};
        }

        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
        if (format == null) { // should never be null, because to be returned by findSchematicFile, the format needs to be not-null
            Utils.log("&cRan into a problem getting the format of the schematic using " + name);
            return () -> {};
        }

        return () -> {
            try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile))) {
                Clipboard clipboard = reader.read();
                try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(location.getWorld()))) {
                    ClipboardHolder holder = new ClipboardHolder(clipboard);
                    if (rotation != 0)
                        holder.setTransform(new AffineTransform().rotateY(rotation));
                    Operation operation = holder
                            .createPaste(session)
                            .to(Utils.toBlockVector3(location))
                            .maskSource(Utils.maskFrom(preMask, Utils.contextFrom(session, location.getWorld())))
                            .ignoreAirBlocks(ignoreAir)
                            .copyEntities(pasteEntities)
                            .copyBiomes(pasteBiomes)
                            .build();
                    Operations.completeLegacy(operation);
                }
            } catch (IOException | WorldEditException e) {
                Utils.log("&cRan into a problem pasting the schematic using " + name + ": " + e.getMessage());
            }
        };
    }

    public static Runnable getDeleteRunnable(String name) {
        File schematicFile = findSchematicFile(name);
        if (schematicFile == null) {
            Utils.log("&cCouldn't find a schematic using " + name);
            return () -> {};
        }

        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
        if (format == null) { // should never be null, because to be returned by findSchematicFile, the format needs to be not-null
            Utils.log("&cRan into a problem getting the format of the schematic using " + name);
            return () -> {};
        }

        return () -> {
            if (!schematicFile.delete())
                Utils.log("&cFailed to delete the schematic named " + name);
        };
    }

}
