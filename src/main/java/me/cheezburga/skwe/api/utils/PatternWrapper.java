package me.cheezburga.skwe.api.utils;

import com.sk89q.worldedit.function.pattern.Pattern;

public record PatternWrapper(Pattern pattern, String asString) {

    @Override
    public String toString() {
        return "worldedit pattern from " + asString;
    }

}
