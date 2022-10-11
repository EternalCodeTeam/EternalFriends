package com.eternalcode.friends.gui;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.profile.Profile;
import com.eternalcode.friends.profile.ProfileManager;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.builder.item.SkullBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FriendListGUI {

    private final MiniMessage miniMessage;
    private final GuiConfig guiConfig;

    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;

    private final Server server;
    private Gui gui;


    public FriendListGUI(MiniMessage miniMessage, GuiConfig guiConfig, ProfileManager profileManager, NotificationAnnouncer announcer, MessagesConfig messages, Server server) {
        this.miniMessage = miniMessage;
        this.guiConfig = guiConfig;
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.messages = messages;
        this.server = server;
    }

    public void openInventory(Player player) {
        Bukkit.getLogger().info("22");
        GuiConfig.Gui guiCfg = guiConfig.friendListGui;
        this.gui = Gui.gui()
                .title(this.miniMessage.deserialize(guiCfg.title))
                .rows(6)
                .disableItemTake()
                .create();

        this.gui.setItem(53, guiConfig.backButton.toGuiItem());

        Optional<Profile> profileOptional = profileManager.getProfileByUUID(player.getUniqueId());
        if (profileOptional.isEmpty()) {
            player.closeInventory();
            announcer.announceMessage(player.getUniqueId(), messages.friends.yourProfileNotFound);
            return;
        }

        int index = 0;
        Profile profile = profileOptional.get();
        for(UUID uuid : profile.getFriends()){
            this.gui.setItem(index,
                    ItemBuilder.skull()
                            .owner(server.getOfflinePlayer(uuid))
                            .asGuiItem()
            );
            index++;
        }

        gui.open(player);

    }

}
