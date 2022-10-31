package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.util.legacy.Legacy;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Contextual
public class ConfigItem {

    public int slot = 0;
    @Description("# Material from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html")
    public Material type = Material.STONE;

    public String name = "&fItem name";

    @Description("# Description of item")
    public List<String> lore = Arrays.asList("&fFirst line of lore", "&9Second line of lore");

    @Description("# Item should be enchanted or not")
    public boolean enchanted = false;

    public GuiItem toGuiItem() {
        ItemBuilder builder = ItemBuilder.from(this.type)
                .name(Legacy.component(this.name))
                .lore(this.lore.stream().map(Legacy::component).collect(Collectors.toList()))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_ENCHANTS);
        if (this.enchanted) {
            builder.enchant(Enchantment.LOOT_BONUS_BLOCKS);
        }
        return builder.asGuiItem();
    }

    public ConfigItem setSlot(int slot) {
        this.slot = slot;
        return this;
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

    public ConfigItem setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
        return this;
    }
}
