package com.eternalcode.friends.gui;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.ConfigItemImpl;
import com.eternalcode.friends.config.implementation.ExampleConfigItem;
import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.friend.FriendManager;
import com.eternalcode.friends.invite.Invite;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.packet.NameTagService;
import com.eternalcode.friends.util.AdventureUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.UUID;

public class ReceivedInvitesGui {

    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final GuiConfig guiConfig;
    private final MiniMessage miniMessage;
    private final InviteManager inviteManager;
    private final Server server;
    private final FriendManager friendManager;
    private final NameTagService nameTagService;
    private final MessagesConfig.Friends friendsConfig;

    private static final int NEXT_PAGE_ITEM_SLOT = 53;
    private static final int BACK_PAGE_ITEM_SLOT = 45;
    private static final int BACK_TO_MAIN_ITEM_SLOT = 49;

    public ReceivedInvitesGui(NotificationAnnouncer announcer,
                              MessagesConfig messages,
                              GuiConfig guiConfig,
                              MiniMessage miniMessage,
                              InviteManager inviteManager,
                              Server server,
                              FriendManager friendManager,
                              NameTagService nameTagService) {
        this.announcer = announcer;
        this.messages = messages;
        this.guiConfig = guiConfig;
        this.miniMessage = miniMessage;
        this.inviteManager = inviteManager;
        this.server = server;
        this.friendsConfig = this.messages.friends;
        this.friendManager = friendManager;
        this.nameTagService = nameTagService;
    }

    public void openInventory(Player player, Runnable onBack) {
        final GuiConfig.MenuItems menuItems = this.guiConfig.menuItems;
        PaginatedGui gui = Gui.paginated()
                .title(this.miniMessage.deserialize(this.guiConfig.guis.receivedInvitesGuiTitle))
                .rows(6)
                .pageSize(45)
                .disableItemTake()
                .create();

        ExampleConfigItem nextPageItem = menuItems.nextPageItem;
        GuiItem nextPageButton = ItemBuilder.from(nextPageItem.type)
                .name(AdventureUtil.RESET_ITEM.append(this.miniMessage.deserialize(nextPageItem.name)))
                .lore(nextPageItem.lore.stream().map(line -> AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(line))).toList())
                .asGuiItem(event -> {
                    gui.next();
                });

        ConfigItemImpl previousPageItemCfg = menuItems.previousPageItem;
        GuiItem backPageButton = ItemBuilder.from(previousPageItemCfg.type)
                .name(AdventureUtil.RESET_ITEM.append(this.miniMessage.deserialize(previousPageItemCfg.name)))
                .lore(previousPageItemCfg.lore.stream().map(line -> AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(line))).toList())
                .asGuiItem(event -> {
                    gui.previous();
                });

        ConfigItemImpl backToMainMenuItemCfg = menuItems.backToMainMenuItem;
        GuiItem backToMainMenuButton = ItemBuilder.from(backToMainMenuItemCfg.type)
                .name(AdventureUtil.RESET_ITEM.append(this.miniMessage.deserialize(backToMainMenuItemCfg.name)))
                .lore(backToMainMenuItemCfg.lore.stream().map(line -> AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(line))).toList())
                .asGuiItem(event -> {
                    onBack.run();
                });

        generateHeads(player, gui);

        gui.setItem(NEXT_PAGE_ITEM_SLOT, nextPageButton);
        gui.setItem(BACK_PAGE_ITEM_SLOT, backPageButton);
        gui.setItem(BACK_TO_MAIN_ITEM_SLOT, backToMainMenuButton);
        gui.open(player);
    }

    private void generateHeads(Player player, PaginatedGui gui) {
        UUID playerUuid = player.getUniqueId();

        List<Invite> receivedInvites = this.inviteManager.getReceivedInvites(playerUuid);
        if (receivedInvites == null || receivedInvites.isEmpty()) {
            return;
        }

        for (Invite invite : receivedInvites) {
            if (this.inviteManager.isInviteExpired(invite.getFrom(), invite.getTo())) {
                if (receivedInvites.size() == 1) {
                    player.closeInventory();
                    this.inviteManager.removeInvite(invite.getFrom(), invite.getTo());
                    return;
                }

                this.inviteManager.removeInvite(invite.getFrom(), invite.getTo());

                continue;
            }

            OfflinePlayer offlinePlayer = this.server.getOfflinePlayer(invite.getFrom());
            UUID friendUuid = invite.getFrom();
            GuiItem skull = ItemBuilder.skull()
                    .owner(offlinePlayer)
                    .name(AdventureUtil.RESET_ITEM.append(this.miniMessage.deserialize(this.guiConfig.menuItems.inviteListfriendHead.name.replace("{friend_name}", offlinePlayer.getName()))))
                    .lore(this.guiConfig.menuItems.inviteListfriendHead.lore.stream().map(line -> AdventureUtil.RESET_ITEM.append(miniMessage.deserialize(line))).toList())
                    .asGuiItem();

            skull.setAction(skullClickAction(player, friendUuid, gui));

            gui.addItem(skull);
        }

        gui.update();
    }

    private GuiAction<InventoryClickEvent> skullClickAction(Player player, UUID friendUuid, PaginatedGui gui) {
        return event -> {
            UUID playerUuid = player.getUniqueId();

            if (event.isLeftClick()) {
                this.friendManager.addFriends(playerUuid, friendUuid);
                this.inviteManager.removeInvite(friendUuid, playerUuid);

                if (this.server.getOfflinePlayer(friendUuid).isOnline()) {
                    this.nameTagService.updateNameTagOfTwoFriends(player, this.server.getPlayer(friendUuid));
                }

                this.announcer.announceMessage(playerUuid, this.friendsConfig.acceptedInvite.replace("{player}", this.server.getOfflinePlayer(friendUuid).getName()));
                this.announcer.announceMessage(friendUuid, this.friendsConfig.yourInvitationHasBeenAccepted.replace("{player}", player.getName()));
            }
            else if (event.isRightClick()) {
                this.inviteManager.removeInvite(friendUuid, playerUuid);
            }

            reloadHeadGui(player, gui);
        };
    }

    private void reloadHeadGui(Player player, PaginatedGui gui) {
        gui.clearPageItems(true);
        generateHeads(player, gui);
    }
}
