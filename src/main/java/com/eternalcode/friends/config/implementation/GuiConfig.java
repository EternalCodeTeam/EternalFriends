package com.eternalcode.friends.config.implementation;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import org.bukkit.Material;

import java.util.List;

@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class GuiConfig extends OkaeriConfig {

    public MenuItems menuItems = new MenuItems();
    public Guis guis = new Guis();

    public static class MenuItems extends OkaeriConfig {
        @Comment("# options of {status} placeholder")
        public OnlineStatus onlineStatus =  new OnlineStatus();

        public FriendHead friendListHead = new FriendHead().setName("{status} <white>{friend_name}")
                .setLore(List.of("", "<red>Click LMB", "<red>to remove a friend", ""));
        public FriendHead inviteListfriendHead = new FriendHead()
                .setLore(List.of("", "<green>Click LMB to accept invitation", "<red>Click RMB to decline invitation", ""));

        public ExampleConfigItem nextPageItem = new ExampleConfigItem()
                .setName("<green>Next page")
                .setType(Material.PAPER)
                .setLore(List.of(
                        "",
                        "<gray>Click to go to the next page",
                        ""
                ));

        public ConfigItemImpl previousPageItem = new ConfigItemImpl()
                .setName("<red>Previous page")
                .setType(Material.PAPER)
                .setLore(List.of(
                        "",
                        "<gray>Click to go to the previous page",
                        ""
                ));

        public ConfigItemImpl confirmItem = new ConfigItemImpl()
                .setName("<green>Yes")
                .setType(Material.LIME_STAINED_GLASS_PANE)
                .setLore(List.of(
                        "",
                        "<gray>Click to confirm",
                        ""
                ));

        public ConfigItemImpl denyItem = new ConfigItemImpl()
                .setName("<red>No")
                .setType(Material.RED_STAINED_GLASS_PANE)
                .setLore(List.of(
                        "",
                        "<gray>Click to cancel",
                        ""
                ));

        public ConfigItemImpl receivedInvitesItem = new ConfigItemImpl()
                .setName("<green>Received invitations")
                .setType(Material.BOOK)
                .setLore(List.of(
                        "",
                        "<gray>Click to open",
                        ""
                ));

        public ConfigItemImpl sendInvitesItem = new ConfigItemImpl()
                .setName("<green>Sending invitations")
                .setType(Material.WRITABLE_BOOK)
                .setLore(List.of(
                        "",
                        "<gray>Click to open",
                        ""
                ));

        public ConfigItemImpl settingItem = new ConfigItemImpl()
                .setName("<green>Settings")
                .setType(Material.REPEATER)
                .setLore(List.of(
                        "",
                        "<gray>Click to open",
                        ""
                ));

        public ConfigItemImpl backToMainMenuItem = new ConfigItemImpl()
                .setName("<gray>Back to main menu")
                .setType(Material.ARROW)
                .setLore(List.of());

        public CloseItem closeItem = new CloseItem()
                .setName("<red>Close menu")
                .setType(Material.BARRIER)
                .setLore(List.of());
    }

    public static class Guis extends OkaeriConfig {
        public String mainGuiTitle = "<aqua>Friends";
        public String confirmGuiTitle = "<red>Are you sure?";
        public String receivedInvitesGuiTitle = "<aqua>Received invitations";
    }

    public static class FriendHead extends OkaeriConfig {
        public String name = "<white>{friend_name}";
        public List<String> lore = List.of("<white>First line of lore", "<blue>Second line of lore");

        public FriendHead setName(String name) {
            this.name = name;
            return this;
        }

        public FriendHead setLore(List<String> lore) {
            this.lore = lore;
            return this;
        }
    }

    public static class OnlineStatus extends OkaeriConfig {
        public String online = "<green>●";
        public String offline = "<gray>●";
    }
}
