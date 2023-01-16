package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.command.CommandSender;

@Route(name = "friends")
public class FriendHelpCommand {

    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;

    public FriendHelpCommand(NotificationAnnouncer announcer, MessagesConfig messages) {
        this.announcer = announcer;
        this.messages = messages;
    }

    @Execute(route = "help")
    @Permission("eternalfriends.access.help")
    public void help(CommandSender sender) {
        for (String message : messages.friends.helpCommand) {
            announcer.announceMessage(sender, message);
        }
    }
}
