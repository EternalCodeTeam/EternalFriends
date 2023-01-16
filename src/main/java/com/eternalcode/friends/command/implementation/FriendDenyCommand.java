package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.profile.ProfileManager;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

import java.util.UUID;

@Route(name = "friends")
public class FriendDenyCommand {

    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final InviteManager inviteManager;
    private final MessagesConfig messages;

    public FriendDenyCommand(ProfileManager profileManager, NotificationAnnouncer announcer, InviteManager inviteManager, MessagesConfig messages) {
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.inviteManager = inviteManager;
        this.messages = messages;
    }

    @Execute(route = "deny", required = 1)
    @Permission("eternalfriends.access.deny")
    public void deny(Player sender, @Arg @Name("player") Player target) {
        MessagesConfig.Friends friends = this.messages.friends;

        if (sender.equals(target)) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);

            return;
        }

        UUID senderUuid = sender.getUniqueId();
        UUID targetUuid = target.getUniqueId();

        if (this.profileManager.hasProfile(targetUuid) || this.profileManager.hasProfile(senderUuid)) {

            return;
        }

        if (inviteManager.hasReceivedInvite(targetUuid, senderUuid)) {
            this.inviteManager.removeInvite(targetUuid, senderUuid);
            this.announcer.announceMessage(senderUuid, friends.inviteDenied.replace("{player}", target.getName()));

            return;
        }

        this.announcer.announceMessage(sender.getUniqueId(), friends.inviteNotFound);
    }
}
