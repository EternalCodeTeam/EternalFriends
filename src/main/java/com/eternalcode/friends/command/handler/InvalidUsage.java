package com.eternalcode.friends.command.handler;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Formatter;

import java.util.List;

public class InvalidUsage implements InvalidUsageHandler<CommandSender> {

    private final MessagesConfig messages;
    private final NotificationAnnouncer announcer;

    public InvalidUsage(MessagesConfig messages, NotificationAnnouncer announcer) {
        this.messages = messages;
        this.announcer = announcer;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, Schematic schematic) {
        List<String> schematics = schematic.getSchematics();

        if (schematic.isOnlyFirst()) {
            this.announcer.announceMessage(sender, this.messages.argument.invalidUsage);
            return;
        }

        for (String scheme : schematics) {
            Formatter formatter = new Formatter()
                    .register("{USAGE}", scheme);

            this.announcer.announceMessage(sender, formatter.format(this.messages.argument.invalidUsage));
        }
    }
}
