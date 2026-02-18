package me.cheezburga.skwe.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.cheezburga.skwe.api.utils.Utils;
import me.cheezburga.skwe.api.utils.schematics.SchematicUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Schematic - Exists")
@Description("Checks whether a schematic with a given name exists.")
@Examples("if schematic named \"example\" exists:")
@Since("1.1.3")
@RequiredPlugins("WorldEdit")
public class CondSchematicExists extends Condition {

	static {
		Skript.registerCondition(CondSchematicExists.class,
			"schem[atic] [named] %string% exists",
			"schem[atic] [named] %string% does(n't| not) exist");
	}

	private Expression<String> name;

	@Override
	@SuppressWarnings({"unchecked"})
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		name = (Expression<String>) exprs[0];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		String name = this.name.getSingle(event);
		if (name == null) {
			error("The provided name was not set!", Utils.toHighlight(this.name));
			return false;
		}
		boolean exists = SchematicUtils.findSchematicFile(name) != null;
		return isNegated() != exists;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "schematic " + name.toString(event, debug) + (isNegated() ? " does not exist" : " exists");
	}

}
