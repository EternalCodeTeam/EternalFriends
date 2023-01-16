package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.profile.Profile;
import com.eternalcode.friends.profile.ProfileManager;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

import java.util.Optional;

@Route(name = "friends")
public class FriendInviteCommand {

    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final InviteManager inviteManager;
    private final MessagesConfig messages;

    public FriendInviteCommand(ProfileManager profileManager, NotificationAnnouncer announcer, InviteManager inviteManager, MessagesConfig messages) {
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.inviteManager = inviteManager;
        this.messages = messages;
    }

    @Execute(route = "invite", required = 1)
    @Permission("eternalfriends.access.invite")
    public void invite(Player sender, @Arg @Name("player") Player target) {
        MessagesConfig.Friends friends = this.messages.friends;

        if (sender.equals(target)) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);

            return;
        }

        Optional<Profile> senderOptional = this.profileManager.getProfileByUUID(sender.getUniqueId());
        Optional<Profile> targetOptional = this.profileManager.getProfileByUUID(target.getUniqueId());

        if (targetOptional.isEmpty()) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.profileNotFound);

            return;
        }
        if (senderOptional.isEmpty()) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.yourProfileNotFound);

            return;
        }

        Profile senderProfile = senderOptional.get();
        Profile targetProfile = targetOptional.get();

        if (targetProfile.isIgnoredPlayer(sender.getUniqueId())) {
            return;
        }

        if (senderProfile.getFriends().contains(target.getUniqueId())) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.alreadyFriend);

            return;
        }

        if (inviteManager.hasReceivedInvite(target.getUniqueId(), sender.getUniqueId()) || this.inviteManager.hasSendedInvite(target.getUniqueId(), sender.getUniqueId())) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.alreadyReceivedInvite);

            return;
        }
        if (inviteManager.hasSendedInvite(sender.getUniqueId(), target.getUniqueId()) || this.inviteManager.hasReceivedInvite(sender.getUniqueId(), target.getUniqueId())) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.alreadySentInvite);

            return;
        }

        this.inviteManager.addInvite(sender.getUniqueId(), target.getUniqueId());

        this.announcer.announceMessage(sender.getUniqueId(), friends.inviteSent.replace("{invited}", target.getName()));
        this.announcer.announceMessage(target.getUniqueId(), friends.inviteReceived.replace("{player}", sender.getName()));
    }
}
