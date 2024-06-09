package me.cheezburga.skwe.elements.sections;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SecCreatePattern extends Section {

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
        return false;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        return null;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return null;
    }
}
