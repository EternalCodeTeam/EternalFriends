package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.ConfigurationService;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.command.CommandSender;

@Route(name = "friends")
public class FriendReloadCommand {

    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final MessagesConfig.Friends friendsConfig;
    private final ConfigurationService configurationService;

    public FriendReloadCommand(NotificationAnnouncer announcer, MessagesConfig messages, ConfigurationService configurationService) {
        this.announcer = announcer;
        this.messages = messages;
        this.configurationService = configurationService;
        this.friendsConfig = this.messages.friends;
    }

    @Execute(route = "reload")
    @Permission("eternalfriends.admin.reload")
    public void reload(CommandSender sender) {
        this.configurationService.reload();

        this.announcer.announceMessage(sender, friendsConfig.configReloaded);
    }
}
