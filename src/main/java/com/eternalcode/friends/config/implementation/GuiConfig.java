package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.config.ReloadableConfig;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import net.kyori.adventure.text.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class GuiConfig implements ReloadableConfig {
    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "gui.yml");
    }

    public MainGui mainGui = new MainGui();
    public FriendListItem friendListItem = new FriendListItem();
    public ReceivedAndSentInvitesItem receivedAndSentInvitesItem = new ReceivedAndSentInvitesItem();
    public SendInvitesItem sendInvitesItem = new SendInvitesItem();
    public SettingItem  settingItem = new SettingItem();

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
    public static class FriendListItem {
        @Description("# Material from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html")
        public String type = "BOOK";

        public String name = "&3Friend List";

        public List<String> lore = Arrays.asList("&fFirst line of lore", "&9Second line of lore");

        public boolean enchanted = false;
    }

    @Contextual
    public static class ReceivedAndSentInvitesItem {
        public String type = "WRITABLE_BOOK";

        public String name = "&7Received and Sent Invites";

        public List<String> lore = Arrays.asList("&fFirst line of lore", "&9Second line of lore");

        public boolean enchanted = false;
    }

    @Contextual
    public static class SendInvitesItem {
        public String type = "PAPER";

        public String name = "&aSend Invites";

        public List<String> lore = Arrays.asList("&fFirst line of lore", "&9Second line of lore");

        public boolean enchanted = false;
    }

    @Contextual
    public static class SettingItem {
        public String type = "REPEATER";

        public String name = "&8Settings";

        public List<String> lore = Arrays.asList("&fFirst line of lore", "&9Second line of lore");

        public boolean enchanted = false;
    }
}
