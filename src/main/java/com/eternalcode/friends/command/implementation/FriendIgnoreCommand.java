package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
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
public class FriendIgnoreCommand {

    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;

    public FriendIgnoreCommand(ProfileManager profileManager, NotificationAnnouncer announcer, MessagesConfig messages) {
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.messages = messages;
    }

    @Execute(route = "ignore", required = 1)
    @Permission("eternalfriends.access.ignore")
    public void ignore(Player sender, @Arg @Name("player") Player target) {
        MessagesConfig.Friends friends = this.messages.friends;

        if (sender.equals(target)) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);

            return;
        }

        Optional<Profile> senderOptional = this.profileManager.getProfileByUUID(sender.getUniqueId());

        if (senderOptional.isEmpty()) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.yourProfileNotFound);

            return;
        }

        Profile senderProfile = senderOptional.get();

        if (senderProfile.isIgnoredPlayer(target.getUniqueId())) {
            senderProfile.removeIgnoredPlayer(target.getUniqueId());
            this.announcer.announceMessage(sender.getUniqueId(), friends.youUnignoredPlayer.replace("{player}", target.getName()));

            return;
        }

        senderProfile.addIgnoredPlayer(target.getUniqueId());

        this.announcer.announceMessage(sender.getUniqueId(), friends.youIgnoredPlayer.replace("{player}", target.getName()));
    }
}
