package me.cheezburga.skwe.api.utils.schematics;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Runnables {

    public static Runnable getSaveRunnable(RegionWrapper wrapper, ClipboardFormat format, String name, @Nullable Location centre, boolean shouldOverwrite) {
        String filePath = WorldEdit.getInstance().getConfiguration().getWorkingDirectoryPath().toString() + "\\schematics\\" + name + "." + format.getPrimaryFileExtension();
        Utils.log(filePath);
        return () -> {
            if (shouldOverwrite) {
                try {
                    Files.deleteIfExists(Paths.get(filePath));
                } catch (IOException e) {
                    Utils.log("&cRan into an exception: " + e.getMessage());
                }
            }

            File file = new File(filePath);
            try (ClipboardWriter writer = format.getWriter(new FileOutputStream(file))) {
                Clipboard clipboard = new BlockArrayClipboard(wrapper.region());
                if (centre != null)
                    clipboard.setOrigin(Utils.blockVector3From(centre));
                ForwardExtentCopy copy = new ForwardExtentCopy(BukkitAdapter.adapt(wrapper.world()), wrapper.region(), clipboard, wrapper.region().getMinimumPoint());
                Operations.complete(copy);
                writer.write(clipboard);
            } catch (IOException | WorldEditException e) {
                Utils.log("&cError" + e.getMessage());
            }
        };
    }

}
