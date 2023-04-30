package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.util.AdventureUtil;
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
        return ItemBuilder.from(this.type)
                .name(AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(this.name)))
                .lore(this.lore.stream().map(line -> AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(line))).toList())
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_ENCHANTS)
                .asGuiItem();
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
