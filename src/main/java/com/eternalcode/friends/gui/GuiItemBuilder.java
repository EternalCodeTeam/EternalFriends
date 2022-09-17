package com.eternalcode.friends.gui;

import com.eternalcode.friends.config.implementation.GuiConfig;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GuiItemBuilder {

    private final GuiItem guiItem;
    private final MiniMessage miniMessage;

    public GuiItemBuilder(GuiConfig.ConfigItem configItem, MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
        ItemBuilder builder = ItemBuilder.from(configItem.type)
                .name(this.miniMessage.deserialize(configItem.name))
                .lore(configItem.lore.stream().map(this.miniMessage::deserialize).collect(Collectors.toList()))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_ENCHANTS);
        if (configItem.enchanted) {
            builder.enchant(Enchantment.LOOT_BONUS_BLOCKS);
        }
        this.guiItem = builder.asGuiItem();
    }

    public GuiItem get() {
        return guiItem;
    }
}
