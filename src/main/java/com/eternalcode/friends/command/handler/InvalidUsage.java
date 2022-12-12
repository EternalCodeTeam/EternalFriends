package com.eternalcode.friends.command.handler;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvalidUsage implements InvalidUsageHandler<CommandSender> {

    private final MessagesConfig messages;
    private final NotificationAnnouncer announcer;

    public InvalidUsage(MessagesConfig messages, NotificationAnnouncer announcer) {
        this.messages = messages;
        this.announcer = announcer;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, Schematic schematic) {

        Player player = (Player) sender;

        schematic.getSchematics().stream().forEach(str -> {
            this.announcer.announceMessage(sender, str);
        });

        this.announcer.announceMessage(sender, messages.argument.invalidUsage);
    }
}
