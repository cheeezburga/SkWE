package me.cheezburga.skwe.api.utils.schematics;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
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
        String filePath = WorldEdit.getInstance().getConfiguration().getWorkingDirectoryPath().toString() + "\\" + name + "." + format.getPrimaryFileExtension();
        Utils.log(filePath); // this doesnt work
        return () -> {
            if (shouldOverwrite) {
                try {
                    Files.deleteIfExists(Paths.get(filePath));
                } catch (IOException e) {
                    Utils.log(Utils.PLUGIN_PREFIX + "&cRan into an exception: " + e.getMessage());
                }
            }

            File file = new File(filePath);
            try (ClipboardWriter writer = format.getWriter(new FileOutputStream(file))) {
                Clipboard clipboard = new BlockArrayClipboard(wrapper.region());
                if (centre != null)
                    clipboard.setOrigin(Utils.blockVector3From(centre));
                writer.write(clipboard);
            } catch (IOException ignored) {}
        };
    }

}
