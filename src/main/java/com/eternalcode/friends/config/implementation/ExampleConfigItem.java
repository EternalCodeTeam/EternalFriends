package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.util.AdventureUtil;
import com.eternalcode.friends.util.legacy.LegacyColorProcessor;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.entity.Exclude;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.util.List;

@Contextual
public class ExampleConfigItem implements ConfigItem<ExampleConfigItem> {

    @Exclude
    private final MiniMessage miniMessage = MiniMessage.builder()
        .postProcessor(new LegacyColorProcessor())
        .build();

    @Description("# Material from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html")
    public Material type = Material.STONE;

    @Description("# Name of item")
    public String name = "&fItem name";

    @Description("# Description of item")
    public List<String> lore = List.of("&fFirst line of lore", "&9Second line of lore", "&cThird line of lore");

    public GuiItem toGuiItem() {
        return ItemBuilder.from(this.type)
            .name(AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(this.name)))
            .lore(this.lore.stream().map(line -> AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(line))).toList())
            .flags(ItemFlag.HIDE_ATTRIBUTES)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .asGuiItem();
    }

    @Override
    public ExampleConfigItem setType(Material type) {
        this.type = type;
        return this;
    }

    @Override
    public ExampleConfigItem setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ExampleConfigItem setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }
}
