package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.config.ReloadableConfig;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import org.bukkit.Material;

import java.io.File;
import java.util.List;

public class GuiConfig implements ReloadableConfig {

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "gui.yml");
    }

    public MenuItems menuItems = new MenuItems();
    public Guis guis = new Guis();

    @Contextual
    public static class MenuItems {
        @Description("# options of {status} placeholder")
        public OnlineStatus onlineStatus = new OnlineStatus();

        public FriendHead friendListHead = new FriendHead()
                .setLore(List.of("", "&cClick LMB", "&cto remove a friend", ""));
        public FriendHead inviteListfriendHead = new FriendHead()
                .setLore(List.of("", "&aClick LMB to accept invitation", "&cClick RMB to decline invitation", ""));

        public ConfigItem nextPageItem = new ConfigItem()
                .setName("&aNext page")
                .setType(Material.PAPER)
                .setLore(List.of(
                        "",
                        "&7Click to go to the next page",
                        ""
                ));

        public ConfigItem previousPageItem = new ConfigItem()
                .setName("&cPrevious page")
                .setType(Material.PAPER)
                .setLore(List.of(
                        "",
                        "&7Click to go to the previous page",
                        ""
                ));

        public ConfigItem confirmItem = new ConfigItem()
                .setName("&aYes")
                .setType(Material.LIME_STAINED_GLASS_PANE)
                .setLore(List.of(
                        "",
                        "&7Click to confirm",
                        ""
                ));

        public ConfigItem denyItem = new ConfigItem()
                .setName("&cNo")
                .setType(Material.RED_STAINED_GLASS_PANE)
                .setLore(List.of(
                        "",
                        "&7Click to cancel",
                        ""
                ));

        public ConfigItem receivedInvitesItem = new ConfigItem()
                .setName("&aReceived invitations")
                .setType(Material.BOOK)
                .setLore(List.of(
                        "",
                        "&7Click to open",
                        ""
                ));
        public ConfigItem sendInvitesItem = new ConfigItem()
                .setName("&aSending invitations")
                .setType(Material.WRITABLE_BOOK)
                .setLore(List.of(
                        "",
                        "&7Click to open",
                        ""
                ));
        public ConfigItem settingItem = new ConfigItem()
                .setName("&aSettings")
                .setType(Material.REPEATER)
                .setLore(List.of(
                        "",
                        "&7Click to open",
                        ""
                ));
        public ConfigItem backToMainMenuItem = new ConfigItem()
                .setName("&7Back to main menu")
                .setType(Material.ARROW)
                .setLore(List.of());
    }

    @Contextual
    public static class Guis {
        public String mainGuiTitle = "&bFriends";
        public String confirmGuiTitle = "&cAre you sure?";
        public String receivedInvitesGuiTitle = "&bReceived invitations";
    }

    @Contextual
    public static class FriendHead {
        public String name = "{status} &f{friend_name}";
        public List<String> lore = List.of("&fFirst line of lore", "&9Second line of lore");

        public FriendHead setLore(List<String> lore) {
            this.lore = lore;
            return this;
        }
    }

    @Contextual
    public static class OnlineStatus {
        public String online = "&a●";
        public String offline = "&7●";
    }
}