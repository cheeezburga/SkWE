package me.cheezburga.skwe.elements.expressions.regions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.sk89q.worldedit.regions.CuboidRegion;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Region - Walls")
@Description({
        "Gets the walls of a given region. This expression only works on cuboid regions.",
        "This returns another region, which can be used in other operations."
})
@Examples(
        "set {walls} to region walls of {cuboid}"
)
public class ExprWalls extends SimplePropertyExpression<RegionWrapper, RegionWrapper> {

    static {
        register(ExprWalls.class, RegionWrapper.class, "[region] walls", "worldeditregion");
    }

    @Override
    public @Nullable RegionWrapper convert(RegionWrapper wrapper) {
        if (wrapper.region() instanceof CuboidRegion cuboid)
            return new RegionWrapper(cuboid.getWalls(), wrapper.world());
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "region walls";
    }

    @Override
    public @NotNull Class<? extends RegionWrapper> getReturnType() {
        return RegionWrapper.class;
    }
}
