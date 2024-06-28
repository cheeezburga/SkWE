package me.cheezburga.skwe.elements.expressions.regions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.sk89q.worldedit.regions.CuboidRegion;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprFaces extends SimplePropertyExpression<RegionWrapper, RegionWrapper> {

    static {
        register(ExprFaces.class, RegionWrapper.class, "[region] faces", "worldeditregion");
    }

    @Override
    public @Nullable RegionWrapper convert(RegionWrapper wrapper) {
        if (wrapper.region() instanceof CuboidRegion cuboid)
            return new RegionWrapper(cuboid.getFaces(), wrapper.world());
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "region faces";
    }

    @Override
    public @NotNull Class<? extends RegionWrapper> getReturnType() {
        return RegionWrapper.class;
    }
}
