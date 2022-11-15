package com.eternalcode.friends.gui;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.ConfigItem;
import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.profile.Profile;
import com.eternalcode.friends.profile.ProfileManager;
import com.eternalcode.friends.util.legacy.Legacy;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.ScrollType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import dev.triumphteam.gui.guis.ScrollingGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public class MainGui {

    private final MiniMessage miniMessage;
    private final GuiConfig guiConfig;
    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final ConfirmGui confirmGUI;
    private final Plugin plugin;

    private final Server server;

    private static final int SETTINGS_ITEM_SLOT = 49;
    private static final int NEXT_PAGE_ITEM_SLOT = 53;
    private static final int BACK_PAGE_ITEM_SLOT = 45;
    private static final int RECEIVED_ITEM_SLOT = 48;
    private static final int SEND_ITEM_SLOT = 50;


    public MainGui(MiniMessage miniMessage, GuiConfig guiConfig, Plugin plugin, ProfileManager profileManager, NotificationAnnouncer announcer, MessagesConfig messages) {
        this.miniMessage = miniMessage;
        this.guiConfig = guiConfig;
        this.plugin = plugin;
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.messages = messages;
        this.server = plugin.getServer();
        this.confirmGUI = new ConfirmGui(this.guiConfig);
    }

    public void openMainGui(Player player) {
        GuiConfig.MainGui mainGui = guiConfig.mainGui;
        PaginatedGui gui = Gui.paginated()
                .title(this.miniMessage.deserialize(mainGui.title))
                .rows(6)
                .pageSize(45)
                .disableItemTake()
                .create();

        GuiItem nextPageButton = ItemBuilder.from(Material.PAPER)
                .name(this.miniMessage.deserialize(this.guiConfig.mainGui.nextPageItemName))
                .lore(this.guiConfig.mainGui.nextPageItemLore.stream().map(string -> this.miniMessage.deserialize(string)).collect(Collectors.toList()))
                .asGuiItem(event -> {
                    gui.next();
                });

        GuiItem backPageButton = ItemBuilder.from(Material.PAPER)
                .name(this.miniMessage.deserialize(this.guiConfig.mainGui.previousPageItemName))
                .lore(this.guiConfig.mainGui.previousPageItemLore.stream().map(string -> this.miniMessage.deserialize(string)).collect(Collectors.toList()))
                .asGuiItem(event -> {
                    gui.previous();
                });

        GuiItem sendInvitesItem = this.guiConfig.sendInvitesItem.toGuiItem();
        sendInvitesItem.setAction(event -> {
            player.closeInventory();
            this.announcer.announceMessage(player.getUniqueId(), this.messages.friends.inviteInstruction);
        });

        GuiItem receivedInvitesItem = this.guiConfig.receivedInvitesItem.toGuiItem();
        receivedInvitesItem.setAction(event -> {
            //TODO
        });

        GuiItem settingsItem = this.guiConfig.settingItem.toGuiItem();
        settingsItem.setAction(event -> {
            //TODO
        });

        generateFriendsHeads(player, gui);

        gui.getFiller().fillBottom(ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem());
        gui.setItem(RECEIVED_ITEM_SLOT, receivedInvitesItem);
        gui.setItem(SEND_ITEM_SLOT, sendInvitesItem);
        gui.setItem(NEXT_PAGE_ITEM_SLOT, nextPageButton);
        gui.setItem(BACK_PAGE_ITEM_SLOT, backPageButton);
        gui.setItem(SETTINGS_ITEM_SLOT, settingsItem);

        gui.open(player);
    }

    private void generateFriendsHeads(Player player, PaginatedGui gui) {
        Optional<Profile> profileOptional = profileManager.getProfileByUUID(player.getUniqueId());
        if (profileOptional.isEmpty()) {
            player.closeInventory();
            announcer.announceMessage(player.getUniqueId(), messages.friends.yourProfileNotFound);
            return;
        }

        Profile profile = profileOptional.get();
        for (UUID uuid : profile.getFriends()) {
            final GuiItem skull = ItemBuilder.skull()
                    .owner(server.getOfflinePlayer(uuid))
                    .name(this.miniMessage.deserialize(guiConfig.friendHead.name.replace("%friend_name%", server.getOfflinePlayer(uuid).getName())))
                    .lore(guiConfig.friendHead.lore.stream().map(Legacy::component).collect(Collectors.toList()))
                    .asGuiItem();
            skull.setAction(event -> {
                this.confirmGUI.openInventory(player, p -> {
                    Optional<Profile> friendProfileOptional = profileManager.getProfileByUUID(uuid);
                    if (friendProfileOptional.isEmpty()) {
                        player.closeInventory();
                        announcer.announceMessage(player.getUniqueId(), messages.friends.profileNotFound);
                    }
                    Profile friendProfile = friendProfileOptional.get();

                    profile.removeFriend(uuid);
                    friendProfile.removeFriend(profile.getUuid());

                    announcer.announceMessage(player.getUniqueId(), messages.friends.youKickedFriend.replace("{player}", server.getOfflinePlayer(uuid).getName()));

                    if (server.getOfflinePlayer(uuid).isOnline()) {
                        announcer.announceMessage(uuid, messages.friends.friendKickedYou.replace("{player}", player.getName()));
                    }

                    player.closeInventory();
                }, p -> openMainGui(player));
            });
            gui.addItem(skull);
        }
    }
}