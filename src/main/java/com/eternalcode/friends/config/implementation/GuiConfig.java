package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.config.ReloadableConfig;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class GuiConfig implements ReloadableConfig {
    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "gui.yml");
    }

    public MainGui mainGui = new MainGui();
    public ConfigItem friendListItem = new ConfigItem();
    public ConfigItem receivedAndSentInvitesItem = new ConfigItem();
    public ConfigItem sendInvitesItem = new ConfigItem();
    public ConfigItem settingItem = new ConfigItem();

    @Contextual
    public static class MainGui {
        @Description("# Rows of inventory (up to 6)")
        public int rows = 3;

        public String title = "&bFriends";

        public int friendListItemSlot = 11;

        public int receivedAndSentInvitesItemSlot = 13;

        public int sendInviteItemSlot = 15;

        public int settingsItemSlot = 18;
    }

    @Contextual
    public static class ConfigItem {
        @Description("# Material from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html")
        public Material type = Material.STONE;

        public String name = "&fItem name";

        public List<String> lore = Arrays.asList("&fFirst line of lore", "&9Second line of lore");

        public boolean enchanted = false;
    }
}
