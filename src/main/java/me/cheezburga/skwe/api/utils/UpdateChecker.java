package me.cheezburga.skwe.api.utils;

import ch.njol.skript.util.Version;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Utility class to check for plugin updates.
 * <p>
 *     This class is copied from SkBee, with the PlayerJoinEvent listener (and associated methods) stripped out.
 *     <a href=https://github.com/ShaneBeee/SkBee/blob/master/src/main/java/com/shanebeestudios/skbee/api/util/UpdateChecker.java">UpdateChecker</a>
 * </p>
 *
 * @author ShaneBee
 */
public class UpdateChecker {

    public static void checkForUpdate(String pluginVersion) {
        Utils.log("Checking for update...");
        getLatestReleaseVersion(version -> {
            Version plugVer = new Version(pluginVersion);
            Version curVer = new Version(version);
            if (curVer.compareTo(plugVer) <= 0) {
                Utils.log("&aSkCheez is up to date!");
            } else {
                Utils.log("&cSkCheez is not up to date!");
                Utils.log(" - Current version: &cv%s", pluginVersion);
                Utils.log(" - Available update: &av%s",version);
                Utils.log(" - Download available at: https://github.com/cheeezburga/SkWE/releases");
            }
        });
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private static void getLatestReleaseVersion(Consumer<String> consumer) {
        try {
            URL url = new URI("https://api.github.com/repos/cheeezburga/SkWE/releases/latest").toURL();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
            String tag_name = jsonObject.get("tag_name").getAsString();
            consumer.accept(tag_name);
        } catch (IOException | URISyntaxException e) {
            Utils.log("&cFailed to check for update!");
            e.printStackTrace();
        }
    }
}