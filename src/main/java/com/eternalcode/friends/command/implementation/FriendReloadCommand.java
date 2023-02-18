package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.ConfigManager;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.command.CommandSender;

@Route(name = "friends")
public class FriendReloadCommand {

    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final ConfigManager configManager;
    private final MessagesConfig.Friends friends;

    public FriendReloadCommand(NotificationAnnouncer announcer, MessagesConfig messages, ConfigManager configManager) {
        this.announcer = announcer;
        this.messages = messages;
        this.configManager = configManager;
        this.friends = this.messages.friends;
    }

    @Execute(route = "reload")
    @Permission("eternalfriends.admin.reload")
    public void reload(CommandSender sender) {
        this.configManager.reload();

        this.announcer.announceMessage(sender, friends.configReloaded);
    }
}
