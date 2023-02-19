package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.friend.FriendManager;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.packet.NameTagService;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

import java.util.UUID;

@Route(name = "friends")
public class FriendAcceptCommand {

    private final NotificationAnnouncer announcer;
    private final InviteManager inviteManager;
    private final MessagesConfig messages;
    private final NameTagService nameTagService;
    private final FriendManager friendManager;
    private final MessagesConfig.Friends friends;

    public FriendAcceptCommand(NotificationAnnouncer announcer, InviteManager inviteManager, MessagesConfig messages, NameTagService nameTagService, FriendManager friendManager) {
        this.announcer = announcer;
        this.inviteManager = inviteManager;
        this.messages = messages;
        this.nameTagService = nameTagService;
        this.friendManager = friendManager;
        this.friends = this.messages.friends;
    }

    @Execute(route = "accept", required = 1)
    @Permission("eternalfriends.access.accept")
    public void accept(Player sender, @Arg @Name("player") Player target) {
        if (sender.equals(target)) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);

            return;
        }

        UUID senderUuid = sender.getUniqueId();
        UUID targetUuid = target.getUniqueId();

        if (!this.inviteManager.hasReceivedInvite(targetUuid, senderUuid)) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.inviteNotFound);
            return;
        }

        if (this.inviteManager.isInviteExpired(targetUuid, senderUuid)) {
            this.inviteManager.removeInvite(targetUuid, senderUuid);

            this.announcer.announceMessage(sender.getUniqueId(), friends.inviteExpired);

            return;
        }

        this.friendManager.addFriends(senderUuid, targetUuid);

        this.nameTagService.updateNameTagOfTwoFriends(sender, target);

        this.inviteManager.removeInvite(targetUuid, senderUuid);

        this.announcer.announceMessage(senderUuid, friends.acceptedInvite.replace("{player}", target.getName()));
        this.announcer.announceMessage(targetUuid, friends.yourInvitationHasBeenAccepted.replace("{player}", sender.getName()));
    }
}
