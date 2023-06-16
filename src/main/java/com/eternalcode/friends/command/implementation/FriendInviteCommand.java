package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.friend.FriendManager;
import com.eternalcode.friends.invite.InviteManager;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

import java.util.UUID;

@Route(name = "friends")
public class FriendInviteCommand {

    private final NotificationAnnouncer announcer;
    private final InviteManager inviteManager;
    private final MessagesConfig messages;
    private final FriendManager friendManager;
    private final MessagesConfig.Friends friendsConfig;

    public FriendInviteCommand(NotificationAnnouncer announcer, InviteManager inviteManager, MessagesConfig messages, FriendManager friendManager) {
        this.announcer = announcer;
        this.inviteManager = inviteManager;
        this.messages = messages;
        this.friendManager = friendManager;
        this.friendsConfig = this.messages.friends;
    }

    @Execute(route = "invite", required = 1)
    @Permission("eternalfriends.access.invite")
    public void invite(Player sender, @Arg @Name("player") Player target) {
        UUID senderUuid = sender.getUniqueId();
        UUID targetUuid = target.getUniqueId();

        if (sender.equals(target)) {
            this.announcer.announceMessage(senderUuid, friendsConfig.yourselfCommand);

            return;
        }

        if (this.friendManager.isIgnoredByPlayer(senderUuid, targetUuid)) {
            this.announcer.announceMessage(senderUuid, friendsConfig.cannotSendInvite);

            return;
        }

        if (this.friendManager.areFriends(senderUuid, targetUuid)) {
            this.announcer.announceMessage(senderUuid, friendsConfig.alreadyFriend);

            return;
        }

        if (inviteManager.hasReceivedInvite(targetUuid, senderUuid) || this.inviteManager.hasSentInvite(targetUuid, senderUuid)) {
            this.announcer.announceMessage(senderUuid, friendsConfig.alreadyReceivedInvite);

            return;
        }
        if (inviteManager.hasSentInvite(senderUuid, targetUuid) || this.inviteManager.hasReceivedInvite(senderUuid, targetUuid)) {
            this.announcer.announceMessage(senderUuid, friendsConfig.alreadySentInvite);

            return;
        }

        this.inviteManager.addInvite(senderUuid, targetUuid);

        this.announcer.announceMessage(senderUuid, friendsConfig.inviteSent.replace("{invited}", target.getName()));
        this.announcer.announceMessage(targetUuid, friendsConfig.inviteReceived.replace("{player}", sender.getName()));
    }
}
