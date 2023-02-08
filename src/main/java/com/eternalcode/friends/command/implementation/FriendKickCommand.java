package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.packet.NameTagService;
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
public class FriendKickCommand {

    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final NameTagService nameTagService;

    public FriendKickCommand(ProfileManager profileManager, NotificationAnnouncer announcer, MessagesConfig messages, NameTagService nameTagService) {
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.messages = messages;
        this.nameTagService = nameTagService;
    }

    @Execute(route = "kick", required = 1)
    @Permission("eternalfriends.access.kick")
    public void kick(Player sender, @Arg @Name("player") Player target){
        MessagesConfig.Friends friends = this.messages.friends;

        if (sender.equals(target)) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);

            return;
        }

        Optional<Profile> targetOptional = profileManager.getProfileByUUID(target.getUniqueId());
        Optional<Profile> senderOptional = profileManager.getProfileByUUID(sender.getUniqueId());

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

        if (!senderProfile.getFriends().contains(target.getUniqueId())) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.playerIsNotYourFriend);

            return;
        }

        senderProfile.removeFriend(target.getUniqueId());
        targetProfile.removeFriend(sender.getUniqueId());

        this.nameTagService.updateNameTagOfTwoNoFriends(sender, target);

        this.announcer.announceMessage(sender.getUniqueId(), friends.youKickedFriend.replace("{player}", target.getName()));
        this.announcer.announceMessage(target.getUniqueId(), friends.friendKickedYou.replace("{player}", sender.getName()));
    }
}
