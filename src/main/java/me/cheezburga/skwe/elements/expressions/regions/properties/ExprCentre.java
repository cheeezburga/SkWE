package me.cheezburga.skwe.elements.expressions.regions.properties;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprCentre extends SimplePropertyExpression<RegionWrapper, Location> {

    static {
        register(ExprCentre.class, Location.class, "region cent(re|er)", "worldeditregions");
    }

    @Override
    public @Nullable Location convert(RegionWrapper wrapper) {
        return Utils.locationFrom(wrapper.region().getCenter().toBlockPoint(), wrapper.world());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "region center";
    }

    @Override
    public @NotNull Class<? extends Location> getReturnType() {
        return Location.class;
    }
}
