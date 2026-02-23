package me.cheezburga.skwe.elements.expressions.regions.properties;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Region - Volume")
@Description("Gets the volume of a given region.")
@Examples(
    "send region volume of {region}"
)
@Since("1.0.0")
@RequiredPlugins("WorldEdit")
public class ExprVolume extends SimplePropertyExpression<RegionWrapper, Number> {

    static {
        register(ExprVolume.class, Number.class, "region volume", "worldeditregions");
    }

    @Override
    public @Nullable Number convert(RegionWrapper wrapper) {
        return wrapper.region().getVolume();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "region volume";
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }

}
