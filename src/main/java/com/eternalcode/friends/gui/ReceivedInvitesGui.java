package com.eternalcode.friends.gui;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.ConfigItem;
import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.invite.Invite;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.profile.Profile;
import com.eternalcode.friends.profile.ProfileManager;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReceivedInvitesGui {

    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final GuiConfig guiConfig;
    private final MiniMessage miniMessage;
    private final InviteManager inviteManager;
    private final Server server;

    private static final int NEXT_PAGE_ITEM_SLOT = 53;
    private static final int BACK_PAGE_ITEM_SLOT = 45;
    private static final int BACK_TO_MAIN_ITEM_SLOT = 49;

    public ReceivedInvitesGui(ProfileManager profileManager, NotificationAnnouncer announcer, MessagesConfig messages, GuiConfig guiConfig, MiniMessage miniMessage, InviteManager inviteManager, Server server) {
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.messages = messages;
        this.guiConfig = guiConfig;
        this.miniMessage = miniMessage;
        this.inviteManager = inviteManager;
        this.server = server;
    }

    public void openInventory(Player player, Runnable onBack) {
        final GuiConfig.MenuItems menuItems = this.guiConfig.menuItems;
        PaginatedGui gui = Gui.paginated()
                .title(this.miniMessage.deserialize(this.guiConfig.guis.receivedInvitesGuiTitle))
                .rows(6)
                .pageSize(45)
                .disableItemTake()
                .create();

        ConfigItem nextPageItem = menuItems.nextPageItem;
        GuiItem nextPageButton = ItemBuilder.from(nextPageItem.type)
                .name(this.miniMessage.deserialize(nextPageItem.name))
                .lore(nextPageItem.lore.stream().map(string -> this.miniMessage.deserialize(string)).toList())
                .asGuiItem(event -> {
                    gui.next();
                });

        ConfigItem previousPageItemCfg = menuItems.previousPageItem;
        GuiItem backPageButton = ItemBuilder.from(previousPageItemCfg.type)
                .name(this.miniMessage.deserialize(previousPageItemCfg.name))
                .lore(previousPageItemCfg.lore.stream().map(string -> this.miniMessage.deserialize(string)).toList())
                .asGuiItem(event -> {
                    gui.previous();
                });

        ConfigItem backToMainMenuItemCfg = menuItems.backToMainMenuItem;
        GuiItem backToMainMenuButton = ItemBuilder.from(backToMainMenuItemCfg.type)
                .name(this.miniMessage.deserialize(backToMainMenuItemCfg.name))
                .lore(backToMainMenuItemCfg.lore.stream().map(string -> this.miniMessage.deserialize(string)).toList())
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
        MessagesConfig.Friends friends = this.messages.friends;

        Optional<Profile> profileOptional = this.profileManager.getProfileByUUID(player.getUniqueId());

        if (profileOptional.isEmpty()) {
            player.closeInventory();
            this.announcer.announceMessage(player.getUniqueId(), friends.yourProfileNotFound);
            return;
        }

        Profile profile = profileOptional.get();

        List<Invite> receivedInvites = this.inviteManager.getReceivedInvites(profile.getUuid());
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
            UUID uuid = invite.getFrom();
            GuiItem skull = ItemBuilder.skull()
                    .owner(offlinePlayer)
                    .name(this.miniMessage.deserialize(this.guiConfig.menuItems.inviteListfriendHead.name.replace("%friend_name%", offlinePlayer.getName())))
                    .lore(this.guiConfig.menuItems.inviteListfriendHead.lore.stream().map(string -> this.miniMessage.deserialize(string)).toList())
                    .asGuiItem();

            skull.setAction(event -> {
                Optional<Profile> friendProfileOptional = this.profileManager.getProfileByUUID(uuid);

                if (friendProfileOptional.isEmpty()) {
                    player.closeInventory();
                    this.announcer.announceMessage(player.getUniqueId(), friends.profileNotFound);
                    return;
                }

                Profile friendProfile = friendProfileOptional.get();

                if (event.isLeftClick()) {
                    profile.addFriend(uuid);
                    friendProfile.addFriend(profile.getUuid());
                    this.inviteManager.removeInvite(uuid, profile.getUuid());
                    this.announcer.announceMessage(profile.getUuid(), friends.acceptedInvite.replace("{player}", this.server.getOfflinePlayer(uuid).getName()));
                    this.announcer.announceMessage(uuid, friends.yourInvitationHasBeenAccepted.replace("{player}", player.getName()));
                }
                else if (event.isRightClick()) {
                    this.inviteManager.removeInvite(uuid, profile.getUuid());
                }
                else {
                    return;
                }

                gui.clearPageItems(true);

                generateHeads(player, gui);
            });
            gui.addItem(skull);
        }
        gui.update();
    }
}
