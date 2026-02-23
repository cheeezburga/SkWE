package me.cheezburga.skwe.elements.expressions.regions.properties;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sk89q.worldedit.regions.Region;
import me.cheezburga.skwe.api.utils.regions.RegionWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Region - Dimensions")
@Description({
    "Gets the dimensions of a given region."
})
@Examples({
    "set {_height} to height of {region}",
    "set {_length} to length of {region}",
    "set {_width} to width of {region}"
})
@Since("1.0.2")
@RequiredPlugins("WorldEdit")
public class ExprDimensions extends SimplePropertyExpression<RegionWrapper, Number> {

    static {
        register(ExprDimensions.class, Number.class, "region (0:height|1:length|2:width)", "worldeditregions");
    }

    private static final int HEIGHT = 0, LENGTH = 1, WIDTH = 2;

    private int dimension;

    @Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.dimension = parseResult.mark;
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Number convert(RegionWrapper wrapper) {
        Region region = wrapper.region();
        return switch (dimension) {
            case HEIGHT -> region.getHeight();
            case LENGTH -> region.getLength();
            case WIDTH -> region.getWidth();
            default -> null;
        };
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "region dimensions";
    }

}
