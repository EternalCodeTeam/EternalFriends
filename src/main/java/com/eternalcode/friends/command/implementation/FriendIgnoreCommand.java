package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.friend.FriendManager;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

import java.util.UUID;

@Route(name = "friends")
public class FriendIgnoreCommand {

    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final FriendManager friendManager;
    private final MessagesConfig.Friends friends;

    public FriendIgnoreCommand(NotificationAnnouncer announcer, MessagesConfig messages, FriendManager friendManager) {
        this.announcer = announcer;
        this.messages = messages;
        this.friendManager = friendManager;
        this.friends = this.messages.friends;
    }

    @Execute(route = "ignore", required = 1)
    @Permission("eternalfriends.access.ignore")
    public void ignore(Player sender, @Arg @Name("player") Player target) {
        UUID targetUuid = target.getUniqueId();
        UUID senderUuid = sender.getUniqueId();


        if (sender.equals(target)) {
            this.announcer.announceMessage(senderUuid, friends.yourselfCommand);

            return;
        }

        if (friendManager.isIgnoredByPlayer(targetUuid, senderUuid)) {
            friendManager.removeIgnoredPlayer(senderUuid, targetUuid);

            this.announcer.announceMessage(senderUuid, friends.youUnignoredPlayer.replace("{player}", target.getName()));

            return;
        }

        friendManager.addIgnoredPlayer(senderUuid, targetUuid);

        this.announcer.announceMessage(senderUuid, friends.youIgnoredPlayer.replace("{player}", target.getName()));
    }
}
