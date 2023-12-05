package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.util.AdventureUtil;
import com.eternalcode.friends.util.legacy.LegacyColorProcessor;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Exclude;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.util.List;

public class CloseItem extends OkaeriConfig implements ConfigurationItem<CloseItem> {

    @Exclude
    private final MiniMessage miniMessage = MiniMessage.builder()
            .postProcessor(new LegacyColorProcessor())
            .build();

    public Material type = Material.STONE;

    public String name = "<red>Close";

    public List<String> lore = List.of("<white>First line of lore");

    @Comment("# List of commands triggered on click")
    @Comment("# Available placeholders: {player}")
    public List<String> commandOnClick = List.of("give {player} stone 1", "tell {player} you received stone!");

    @Override
    public GuiItem toGuiItem() {
        return ItemBuilder.from(this.type)
                .name(AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(this.name)))
                .lore(this.lore.stream().map(line -> AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(line))).toList())
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_ENCHANTS)
                .asGuiItem();
    }

    @Override
    public CloseItem setType(Material type) {
        this.type = type;
        return this;
    }

    @Override
    public CloseItem setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public CloseItem setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }
}
