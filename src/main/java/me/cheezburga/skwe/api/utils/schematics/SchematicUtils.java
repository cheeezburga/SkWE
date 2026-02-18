package me.cheezburga.skwe.api.utils.schematics;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SchematicUtils {

    public static @Nullable File findSchematicFile(String input) {
        File file = new File(input);
        if (file.exists()) {
            if (ClipboardFormats.findByFile(file) != null)
                return file;
        }

        Path directory = Paths.get(WorldEdit.getInstance().getConfiguration().getWorkingDirectoryPath().toString(), "schematics");
        for (ClipboardFormat format : ClipboardFormats.getAll()) {
            File potentialFile = directory.resolve(input + "." + format.getPrimaryFileExtension()).toFile();
            if (potentialFile.exists()) {
                if (ClipboardFormats.findByFile(potentialFile) != null)
                    return potentialFile;
            }
        }

        return null;
    }

}
