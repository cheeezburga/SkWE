package me.cheezburga.skwe.api.utils;

import com.sk89q.worldedit.function.mask.Mask;

public record MaskWrapper(Mask mask, String asString) {

    @Override
    public String toString() {
        return "worldedit mask from " + asString;
    }

}
