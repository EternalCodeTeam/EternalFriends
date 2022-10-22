package com.eternalcode.friends.gui;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.profile.Profile;
import com.eternalcode.friends.profile.ProfileManager;
import com.eternalcode.friends.util.legacy.Legacy;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FriendListGUI {

    private final MiniMessage miniMessage;
    private final GuiConfig guiConfig;

    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;

    private final Server server;

    private ConfirmGUI confirmGui;



    public FriendListGUI(MiniMessage miniMessage, GuiConfig guiConfig, ProfileManager profileManager, NotificationAnnouncer announcer, MessagesConfig messages, Server server) {
        this.miniMessage = miniMessage;
        this.guiConfig = guiConfig;
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.messages = messages;
        this.server = server;
    }



    public void openInventory(Player player, Consumer<Player> backToMainGUI) {
        Gui gui;
        GuiItem backButton;
        confirmGui = new ConfirmGUI(guiConfig);

        GuiConfig.Gui guiCfg = guiConfig.friendListGui;
        gui = Gui.gui()
                .title(this.miniMessage.deserialize(guiCfg.title))
                .rows(6)
                .disableItemTake()
                .create();

        backButton = guiConfig.backButton.toGuiItem();
        backButton.setAction(event -> {
            backToMainGUI.accept(player);
        });
        gui.setItem(53, backButton);

        Optional<Profile> profileOptional = profileManager.getProfileByUUID(player.getUniqueId());
        if (profileOptional.isEmpty()) {
            player.closeInventory();
            announcer.announceMessage(player.getUniqueId(), messages.friends.yourProfileNotFound);
            return;
        }

        int index = 0;
        Profile profile = profileOptional.get();
        for (UUID uuid : profile.getFriends()) {
            final GuiItem skull = ItemBuilder.skull()
                    .owner(server.getOfflinePlayer(uuid))
                    .name(this.miniMessage.deserialize(guiConfig.friendHead.name.replace("%friend_name%", server.getOfflinePlayer(uuid).getName())))
                    .lore(guiConfig.friendHead.lore.stream().map(Legacy::component).collect(Collectors.toList()))
                    .asGuiItem();
            skull.setAction(event -> {
                this.confirmGui.openInventory(player, p -> {
                    profile.removeFriend(uuid);
                    announcer.announceMessage(player.getUniqueId(), messages.friends.youKickedFriend.replace("{player}", server.getOfflinePlayer(uuid).getName()));
                    if(server.getOfflinePlayer(uuid).isOnline()){
                        announcer.announceMessage(uuid, messages.friends.friendKickedYou.replace("{player}", player.getName()));
                    }
                    player.closeInventory();
                }, p -> openInventory(player, backToMainGUI));
            });
            gui.setItem(index,skull);
            index++;
        }
        gui.open(player);

    }

}
