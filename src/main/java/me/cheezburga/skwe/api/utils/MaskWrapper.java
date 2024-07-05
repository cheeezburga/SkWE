package me.cheezburga.skwe.api.utils;

import com.sk89q.worldedit.function.mask.Mask;

import java.util.Arrays;

public record MaskWrapper(Mask mask, Object... input) {

    @Override
    public String toString() {
        return "worldedit mask from " + Arrays.toString(input);
    }

}
