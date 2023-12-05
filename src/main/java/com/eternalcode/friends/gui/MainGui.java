package com.eternalcode.friends.gui;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.CloseItem;
import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.friend.FriendManager;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.packet.NameTagService;
import com.eternalcode.friends.util.AdventureUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;


public class MainGui {

    private static final int SETTINGS_ITEM_SLOT = 49;
    private static final int NEXT_PAGE_ITEM_SLOT = 53;
    private static final int BACK_PAGE_ITEM_SLOT = 45;
    private static final int RECEIVED_ITEM_SLOT = 48;
    private static final int CLOSE_ITEM_SLOT = 49;
    private static final int SEND_ITEM_SLOT = 50;
    private final MiniMessage miniMessage;
    private final GuiConfig guiConfig;
    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final ConfirmGui confirmGUI;
    private final ReceivedInvitesGui receivedInvitesGui;
    private final InviteManager inviteManager;
    private final Plugin plugin;
    private final Server server;
    private final FriendManager friendManager;
    private final NameTagService nameTagService;


    public MainGui(MiniMessage miniMessage,
                   GuiConfig guiConfig,
                   Plugin plugin,
                   NotificationAnnouncer announcer,
                   MessagesConfig messages,
                   InviteManager inviteManager,
                   FriendManager friendManager,
                   NameTagService nameTagService) {
        this.miniMessage = miniMessage;
        this.guiConfig = guiConfig;
        this.plugin = plugin;
        this.friendManager = friendManager;
        this.announcer = announcer;
        this.messages = messages;
        this.inviteManager = inviteManager;
        this.server = plugin.getServer();
        this.nameTagService = nameTagService;
        this.confirmGUI = new ConfirmGui(this.guiConfig, this.miniMessage);
        this.receivedInvitesGui = new ReceivedInvitesGui(this.announcer, this.messages, this.guiConfig, this.miniMessage, this.inviteManager, this.server, this.friendManager, this.nameTagService);
    }

    public void openMainGui(Player player) {
        final GuiConfig.MenuItems menuItems = this.guiConfig.menuItems;

        PaginatedGui gui = Gui.paginated()
            .title(this.miniMessage.deserialize(this.guiConfig.guis.mainGuiTitle))
            .rows(6)
            .pageSize(45)
            .disableItemTake()
            .create();

        GuiItem nextPageButton = ItemBuilder.from(Material.PAPER)
            .name(AdventureUtil.RESET_ITEM.append(this.miniMessage.deserialize(menuItems.nextPageItem.name)))
            .lore(menuItems.nextPageItem.lore.stream().map(line -> AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(line))).toList())
            .asGuiItem(event -> {
                gui.next();
            });

        GuiItem backPageButton = ItemBuilder.from(Material.PAPER)
            .name(AdventureUtil.RESET_ITEM.append(this.miniMessage.deserialize(menuItems.previousPageItem.name)))
            .lore(menuItems.previousPageItem.lore.stream().map(line -> AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(line))).toList())
            .asGuiItem(event -> {
                gui.previous();
            });

        GuiItem sendInvitesItem = menuItems.sendInvitesItem.toGuiItem();
        sendInvitesItem.setAction(event -> {
            player.closeInventory();
            this.announcer.announceMessage(player.getUniqueId(), this.messages.friends.inviteInstruction);
        });

        GuiItem receivedInvitesItem = menuItems.receivedInvitesItem.toGuiItem();
        receivedInvitesItem.setAction(event -> {
            receivedInvitesGui.openInventory(player, () -> {
                this.openMainGui(player);
            });
        });

        CloseItem closeItemConfig = menuItems.closeItem;
        GuiItem closeButton = menuItems.closeItem.toGuiItem();
        closeButton.setAction(event -> {
            HumanEntity whoClicked = event.getWhoClicked();
            whoClicked.closeInventory();

            closeItemConfig.commandOnClick.forEach(command -> {
                this.server.dispatchCommand(this.server.getConsoleSender(), command
                    .replace("{player}", whoClicked.getName()));
            });
        });

        generateFriendsHeads(player, gui);

        gui.getFiller().fillBottom(ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem());
        gui.setItem(RECEIVED_ITEM_SLOT, receivedInvitesItem);
        gui.setItem(SEND_ITEM_SLOT, sendInvitesItem);
        gui.setItem(NEXT_PAGE_ITEM_SLOT, nextPageButton);
        gui.setItem(BACK_PAGE_ITEM_SLOT, backPageButton);
        gui.setItem(CLOSE_ITEM_SLOT, closeButton);

        gui.open(player);
    }

    private void generateFriendsHeads(Player player, PaginatedGui gui) {
        for (UUID friendUuid : this.friendManager.getFriends(player.getUniqueId())) {
            gui.addItem(generateSkull(player, friendUuid));
        }
    }

    private GuiItem generateSkull(Player player, UUID friendUuid) {
        Player friend = this.server.getPlayer(friendUuid);

        boolean isFriendOnline = friend != null && friend.isOnline();

        final GuiItem skull = ItemBuilder.skull()
            .owner(this.server.getOfflinePlayer(friendUuid))
            .name(AdventureUtil.RESET_ITEM.append(this.miniMessage.deserialize(this.guiConfig.menuItems.friendListHead.name
                .replace("{friend_name}", isFriendOnline ? friend.getName() : this.server.getOfflinePlayer(friendUuid).getName())
                .replace("{status}", isFriendOnline ? this.guiConfig.menuItems.onlineStatus.online : this.guiConfig.menuItems.onlineStatus.offline)
            )))
            .lore(this.guiConfig.menuItems.friendListHead.lore.stream()
                .map(line -> AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(line))).toList())
            .asGuiItem();

        skull.setAction(event -> {
            if (!event.isLeftClick()) {
                return;
            }

            this.confirmGUI.openInventory(player, () -> {
                this.friendManager.removeFriends(player.getUniqueId(), friendUuid);

                if (this.server.getOfflinePlayer(friendUuid).isOnline()) {
                    this.nameTagService.updateNameTagOfTwoNoFriends(player, this.server.getPlayer(friendUuid));
                }

                this.announcer.announceMessage(player.getUniqueId(), this.messages.friends.youKickedFriend
                    .replace("{player}", server.getOfflinePlayer(friendUuid).getName()));

                if (server.getOfflinePlayer(friendUuid).isOnline()) {
                    this.announcer.announceMessage(friendUuid, this.messages.friends.friendKickedYou
                        .replace("{player}", player.getName()));
                }

                openMainGui(player);
            }, () -> openMainGui(player));
        });
        return skull;
    }
}
