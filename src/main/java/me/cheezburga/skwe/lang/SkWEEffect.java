package me.cheezburga.skwe.lang;

import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.util.AsyncEffect;
import me.cheezburga.skwe.SkWE;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SkWEEffect extends AsyncEffect {

    private boolean blocking;

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }

    public boolean isBlocking() {
        return this.blocking;
    }

    @Override
    @Nullable
    protected TriggerItem walk(@NotNull Event event) {
        if (SkWE.HAS_FAWE) {
            super.walk(event);
        } else {
            execute(event);
            return getNext();
        }
        return null;
    }

}
