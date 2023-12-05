package com.eternalcode.friends.command.handler;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.handle.PermissionHandler;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Formatter;
import panda.utilities.text.Joiner;

public class PermissionMessage implements PermissionHandler<CommandSender> {

    private final MessagesConfig messages;
    private final NotificationAnnouncer announcer;

    public PermissionMessage(MessagesConfig messages, NotificationAnnouncer announcer) {
        this.messages = messages;
        this.announcer = announcer;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, RequiredPermissions requiredPermissions) {
        String value = Joiner.on(", ")
                .join(requiredPermissions.getPermissions())
                .toString();

        Formatter formatter = new Formatter()
                .register("{PERMISSION}", value);

        this.announcer.announceMessage(sender, formatter.format(this.messages.argument.missingPermission));
    }
}
