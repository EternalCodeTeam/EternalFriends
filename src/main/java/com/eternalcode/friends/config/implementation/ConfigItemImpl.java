package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.util.AdventureUtil;
import com.eternalcode.friends.util.legacy.LegacyColorProcessor;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Exclude;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.util.List;

public class ConfigItemImpl extends OkaeriConfig implements ConfigurationItem<ConfigItemImpl> {

    @Exclude
    private final MiniMessage miniMessage = MiniMessage.builder()
        .postProcessor(new LegacyColorProcessor())
        .build();

    public Material type = Material.STONE;

    public String name = "<white>Item name";

    public List<String> lore = List.of("<white>First line of lore", "<blue>Second line of lore");

    public GuiItem toGuiItem() {
        return ItemBuilder.from(this.type)
            .name(AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(this.name)))
            .lore(this.lore.stream().map(line -> AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(line))).toList())
            .flags(ItemFlag.HIDE_ATTRIBUTES)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .asGuiItem();
    }

    @Override
    public ConfigItemImpl setType(Material type) {
        this.type = type;
        return this;
    }

    @Override
    public ConfigItemImpl setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ConfigItemImpl setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }
}
