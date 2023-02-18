package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.invite.InviteManager;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

import java.util.UUID;

@Route(name = "friends")
public class FriendDenyCommand {

    private final NotificationAnnouncer announcer;
    private final InviteManager inviteManager;
    private final MessagesConfig messages;
    private final MessagesConfig.Friends friends;

    public FriendDenyCommand(NotificationAnnouncer announcer, InviteManager inviteManager, MessagesConfig messages) {
        this.announcer = announcer;
        this.inviteManager = inviteManager;
        this.messages = messages;
        this.friends = this.messages.friends;
    }

    @Execute(route = "deny", required = 1)
    @Permission("eternalfriends.access.deny")
    public void deny(Player sender, @Arg @Name("player") Player target) {
        if (sender.equals(target)) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);

            return;
        }

        UUID senderUuid = sender.getUniqueId();
        UUID targetUuid = target.getUniqueId();

        if (inviteManager.hasReceivedInvite(targetUuid, senderUuid)) {
            this.inviteManager.removeInvite(targetUuid, senderUuid);
            this.announcer.announceMessage(senderUuid, friends.inviteDenied.replace("{player}", target.getName()));

            return;
        }

        this.announcer.announceMessage(sender.getUniqueId(), friends.inviteNotFound);
    }
}
