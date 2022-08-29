package com.eternalcode.friends.util;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public final class ColorUtil {

    private ColorUtil() {}

    public static String colored(String toColor){
        return ChatColor.translateAlternateColorCodes('&', toColor);
    }

    public static List<String> colored(List<String> toColor) {
        return toColor.stream()
                .map(ColorUtil::colored)
                .collect(Collectors.toList());
    }
}
