package me.cheezburga.skwe.api.utils;

import com.sk89q.worldedit.function.pattern.Pattern;

import java.util.Arrays;

public record PatternWrapper(Pattern pattern, Object... input) {

    @Override
    public String toString() {
        return "worldedit pattern from " + Arrays.toString(input);
    }

}
