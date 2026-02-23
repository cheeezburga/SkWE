package me.cheezburga.skwe.api.utils;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.world.World;
import org.bukkit.entity.Player;

public class PlayerUtils {

	public static SessionManager sessionManager = WorldEdit.getInstance().getSessionManager();

	public static LocalSession getLocalSession(Player player) {
		Actor actor = BukkitAdapter.adapt(player);
		return sessionManager.getIfPresent(actor);
	}

	public static Region getSelection(Player player) throws IncompleteRegionException {
		LocalSession session = getLocalSession(player);
		World selectionWorld = session.getSelectionWorld();
		if (selectionWorld == null)
			throw new IncompleteRegionException();
		return session.getSelection(selectionWorld);
	}

	public static BlockVector3 getPos1(Player player) {
		try {
			Region region = getSelection(player);
			return region.getMinimumPoint();
		} catch (IncompleteRegionException e) {
			return null;
		}
	}

	public static BlockVector3 getPos2(Player player) {
		try {
			Region region = getSelection(player);
			return region.getMaximumPoint();
		} catch (IncompleteRegionException e) {
			return null;
		}
	}

	public static ClipboardHolder getClipboard(Player player) {
		LocalSession session = getLocalSession(player);
		try {
			return session.getClipboard();
		} catch (EmptyClipboardException e) {
			return null;
		}
	}

	public static void setClipboard(Player player, Clipboard clipboard) {
		LocalSession session = getLocalSession(player);
		session.setClipboard(new ClipboardHolder(clipboard));
	}

}
