package com.eternalcode.friends;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.UUID;

public class NotificationAnnouncer {

    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;

    public NotificationAnnouncer(AudienceProvider audienceProvider, MiniMessage miniMessage) {
        this.audienceProvider = audienceProvider;
        this.miniMessage = miniMessage;
    }

    public void announceTitle(UUID uniqueId, String title, String subTitle, Duration fadeIn, Duration stay, Duration fadeOut) {
        Audience audience = this.audienceProvider.player(uniqueId);

        Title.Times titleTimes = Title.Times.times(fadeIn, stay, fadeOut);
        Title titlePart = Title.title(this.miniMessage.deserialize(title), this.miniMessage.deserialize(subTitle), titleTimes);

        audience.showTitle(titlePart);
    }

    public void announceActionBar(UUID uniqueId, String message) {
        this.audienceProvider.player(uniqueId).sendActionBar(this.miniMessage.deserialize(message));
    }

    public void announceMessage(UUID uniqueId, String message) {
        this.audienceProvider.player(uniqueId).sendMessage(this.miniMessage.deserialize(message));
    }

    public void announceMessage(CommandSender commandSender, String message) {
        if (commandSender instanceof Player) {
            this.audienceProvider.player(((Player) commandSender).getUniqueId()).sendMessage(this.miniMessage.deserialize(message));
            return;
        }

        this.audienceProvider.console().sendMessage(this.miniMessage.deserialize(message));
    }
}
