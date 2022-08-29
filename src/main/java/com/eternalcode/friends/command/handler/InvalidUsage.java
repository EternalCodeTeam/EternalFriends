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

        if (schematic.getSchematics().size() > 1) {
            for (String scheme : schematic.getSchematics()) {
                this.announcer.announceMessage(player.getUniqueId(), "{scheme}".replace("{scheme}", scheme));
            }
        } else {
            this.announcer.announceMessage(player.getUniqueId(), "{scheme}".replace("{scheme}", schematic.getSchematics().get(0)));
        }
    }
}
