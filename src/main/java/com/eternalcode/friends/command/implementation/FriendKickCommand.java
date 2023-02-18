package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.friend.FriendManager;
import com.eternalcode.friends.packet.NameTagService;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

import java.util.UUID;

@Route(name = "friends")
public class FriendKickCommand {

    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final NameTagService nameTagService;
    private final FriendManager friendManager;
    private final MessagesConfig.Friends friends;

    public FriendKickCommand(NotificationAnnouncer announcer, MessagesConfig messages, NameTagService nameTagService, FriendManager friendManager) {
        this.announcer = announcer;
        this.messages = messages;
        this.nameTagService = nameTagService;
        this.friendManager = friendManager;
        this.friends = this.messages.friends;
    }

    @Execute(route = "kick", required = 1)
    @Permission("eternalfriends.access.kick")
    public void kick(Player sender, @Arg @Name("player") Player target){
        UUID senderUuid = sender.getUniqueId();
        UUID targetUuid = target.getUniqueId();

        if (sender.equals(target)) {
            this.announcer.announceMessage(senderUuid, friends.yourselfCommand);

            return;
        }

        if (!this.friendManager.areFriends(senderUuid, targetUuid)) {
            this.announcer.announceMessage(senderUuid, friends.playerIsNotYourFriend);

            return;
        }

        this.friendManager.removeFriends(senderUuid, targetUuid);


        // nie dziala
        this.nameTagService.updateNameTagOfTwoNoFriends(sender, target);

        this.announcer.announceMessage(senderUuid, friends.youKickedFriend.replace("{player}", target.getName()));
        this.announcer.announceMessage(targetUuid, friends.friendKickedYou.replace("{player}", sender.getName()));
    }
}
