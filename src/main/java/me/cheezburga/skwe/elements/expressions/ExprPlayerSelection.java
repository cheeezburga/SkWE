package me.cheezburga.skwe.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import me.cheezburga.skwe.api.utils.PlayerUtils;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Player Selection")
@Description("Gets a player's current selection. This returns another region, which can be used in other operations.")
@Examples(
	"set {region} to current selection of {player}"
)
@Since("1.1.5")
@RequiredPlugins("WorldEdit")
public class ExprPlayerSelection extends SimplePropertyExpression<Player, RegionWrapper> {

	static {
		register(ExprPlayerSelection.class, RegionWrapper.class, "[current] selection", "player");
	}

	@Override
	public @Nullable RegionWrapper convert(Player player) {
		try {
			Region selection = PlayerUtils.getSelection(player);
			return new RegionWrapper(selection, BukkitAdapter.adapt(selection.getWorld())); // should never be null for a player's selection
		} catch (IncompleteRegionException e) {
			error("Failed to retrieve the selection of " + getExpr() + ", as it was incomplete.", Utils.toHighlight(getExpr()));
			return null;
		}
	}

	@Override
	protected @NotNull String getPropertyName() {
		return "current selection";
	}

	@Override
	public @NotNull Class<? extends RegionWrapper> getReturnType() {
		return RegionWrapper.class;
	}

}
