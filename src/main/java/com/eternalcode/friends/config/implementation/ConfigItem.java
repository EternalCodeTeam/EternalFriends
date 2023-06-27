package com.eternalcode.friends.config.implementation;

import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;

import java.util.List;

public interface ConfigItem<T> {
    T setType(Material type);

    T setName(String name);

    T setLore(List<String> lore);

    GuiItem toGuiItem();
}
