package me.cheezburga.skwe.lang;

import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.util.AsyncEffect;
import me.cheezburga.skwe.SkWE;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class SkWEEffect extends AsyncEffect {

    private boolean delayed;

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public boolean isDelayed() {
        return this.delayed;
    }

    @Override
    @Nullable
    protected TriggerItem walk(Event event) {
        if (this.delayed) {
            super.walk(event);
        } else {
            execute(event);
            return getNext();
        }
        return null;
    }

}
