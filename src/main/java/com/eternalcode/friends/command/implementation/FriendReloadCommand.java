package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.ConfigManager;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.gui.MainGui;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.profile.ProfileManager;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

@Route(name = "friends")
public class FriendReloadCommand {
    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final ConfigManager configManager;

    public FriendReloadCommand(NotificationAnnouncer announcer, MessagesConfig messages, ConfigManager configManager) {
        this.announcer = announcer;
        this.messages = messages;
        this.configManager = configManager;
    }

    @Execute(route = "reload")
    @Permission("eternalfriends.admin.reload")
    public void reload(CommandSender sender) {
        MessagesConfig.Friends friends = messages.friends;

        this.configManager.reload();

        this.announcer.announceMessage(sender, friends.configReloaded);
    }
}
