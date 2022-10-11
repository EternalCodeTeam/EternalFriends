package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.config.ReloadableConfig;
import com.eternalcode.friends.util.legacy.Legacy;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuiConfig implements ReloadableConfig {

    public MainGui mainGui = new MainGui();

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "gui.yml");
    }

    public ConfigItem friendListItem = new ConfigItem();
    public ConfigItem receivedAndSentInvitesItem = new ConfigItem();
    public ConfigItem sendInvitesItem = new ConfigItem();
    public ConfigItem settingItem = new ConfigItem();
    public ConfigItem backButton = new ConfigItem();

    public Gui friendListGui = new Gui();

    @Contextual
    public static class MainGui {
        @Description("# Rows of inventory (up to 6)")
        public int rows = 3;

        @Description("# Title of inventory")
        public String title = "&bFriends";
    }

    @Contextual
    public static class Gui {
        @Description("# Title of inventory")
        public String title = "&bGui Title";
    }

    @Contextual
    public static class ConfigItem {

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
    }
}
