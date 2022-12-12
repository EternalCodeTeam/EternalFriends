package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.ConfigManager;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.gui.MainGui;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.profile.Profile;
import com.eternalcode.friends.profile.ProfileManager;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

@Route(name = "friends")
public class FriendListCommand {
    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final Server server;

    public FriendListCommand(ProfileManager profileManager, NotificationAnnouncer announcer, MessagesConfig messages, Server server) {
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.messages = messages;
        this.server = server;
    }

    @Execute(route = "list")
    @Permission("eternalfriends.access.list")
    public void list(Player sender) {
        MessagesConfig.Friends friends = messages.friends;
        Optional<Profile> senderOptional = this.profileManager.getProfileByUUID(sender.getUniqueId());

        if (senderOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourProfileNotFound);
            return;
        }

        Profile senderProfile = senderOptional.get();

        announcer.announceMessage(sender.getUniqueId(), listOfFriends(friends, senderProfile));
    }

    @Execute(route = "adminlist", required = 1)
    @Permission("eternalfriends.admin.list")
    public void listAdmin(CommandSender sender, @Arg @Name("player") Player target) {
        MessagesConfig.Friends friends = messages.friends;
        Optional<Profile> targetOptional = this.profileManager.getProfileByUUID(target.getUniqueId());

        if (targetOptional.isEmpty()) {
            announcer.announceMessage(sender, friends.profileNotFound);
            return;
        }

        Profile profile = targetOptional.get();

        this.announcer.announceMessage(sender, listOfFriends(friends, profile));
    }

    private String listOfFriends(MessagesConfig.Friends friends, Profile profile) {
        StringBuilder builder = new StringBuilder();

        if (profile.getFriends().size() == 0) {
            builder.append(friends.emptyFriendList);
        }
        else {
            builder.append(friends.friendListHeader);
            for (UUID uuid : profile.getFriends()) {
                builder.append(friends.friendListPlayer.replace("{player}", server.getOfflinePlayer(uuid).getName()));
            }
        }

        return builder.toString();
    }
}
