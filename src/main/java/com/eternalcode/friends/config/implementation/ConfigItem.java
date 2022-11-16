package com.eternalcode.friends.config.implementation;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import java.util.List;

@Contextual
public class ConfigItem {
    @Description("# Material from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html")
    public Material type = Material.STONE;

    public String name = "&fItem name";

    @Description("# Description of item")
    public List<String> lore = List.of("&fFirst line of lore", "&9Second line of lore");

    public GuiItem toGuiItem(MiniMessage miniMessage) {
        ItemBuilder builder = ItemBuilder.from(this.type)
                .name(miniMessage.deserialize(this.name))
                .lore(this.lore.stream().map(string -> miniMessage.deserialize(string)).toList())
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_ENCHANTS);
        return builder.asGuiItem();
    }

    public ConfigItem setType(Material type) {
        this.type = type;
        return this;
    }

    public ConfigItem setName(String name) {
        this.name = name;
        return this;
    }

    public ConfigItem setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }
}
