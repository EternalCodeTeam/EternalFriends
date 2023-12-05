package com.eternalcode.friends.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class AdventureUtil {

    public static final Component RESET_ITEM = Component.text()
        .color(NamedTextColor.WHITE)
        .decoration(TextDecoration.ITALIC, false)
        .build();

    private AdventureUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
