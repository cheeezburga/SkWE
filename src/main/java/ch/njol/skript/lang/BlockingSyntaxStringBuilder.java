package ch.njol.skript.lang;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Extension of {@link SyntaxStringBuilder} that records whether the
 * represented expression is blocking or not. When marked as non-blocking
 * either through the constructor or {@link #blocking(boolean)},
 * {@link #toString()} will append the text " lazily" to the built syntax
 * string.
 */
public class BlockingSyntaxStringBuilder extends SyntaxStringBuilder {

    private boolean blocking = true;

    public BlockingSyntaxStringBuilder(@Nullable Event event, boolean debug) {
        this(event, debug, true);
    }

    public BlockingSyntaxStringBuilder(@Nullable Event event, boolean debug, boolean blocking) {
        super(event, debug);
        this.blocking = blocking;
    }

    /**
     * Marks this builder as blocking or not.
     *
     * @param blocking whether the syntax string should indicate blocking
     * @return this builder
     */
    public BlockingSyntaxStringBuilder blocking(boolean blocking) {
        this.blocking = blocking;
        return this;
    }

    @Override
    public BlockingSyntaxStringBuilder append(@NotNull Object object) {
        super.append(object);
        return this;
    }

    @Override
    public BlockingSyntaxStringBuilder append(@NotNull Object... objects) {
        super.append(objects);
        return this;
    }

    @Override
    public String toString() {
        String result = super.toString();
        return blocking ? result : result + " lazily";
    }
}
