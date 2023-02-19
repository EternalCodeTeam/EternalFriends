package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.friend.FriendManager;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Route(name = "friends")
public class FriendListCommand {

    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final Server server;
    private final FriendManager friendManager;
    private final MessagesConfig.Friends friendsConfig;

    public FriendListCommand(NotificationAnnouncer announcer, MessagesConfig messages, Server server, FriendManager friendManager) {
        this.announcer = announcer;
        this.messages = messages;
        this.server = server;
        this.friendManager = friendManager;
        this.friendsConfig = this.messages.friends;
    }

    @Execute(route = "list")
    @Permission("eternalfriends.access.list")
    public void list(Player sender) {

        this.announcer.announceMessage(sender.getUniqueId(), listOfFriends(sender.getUniqueId()));

    }

    @Execute(route = "adminlist", required = 1)
    @Permission("eternalfriends.admin.list")
    public void listAdmin(CommandSender sender, @Arg @Name("player") Player target) {

        this.announcer.announceMessage(sender, listOfFriends(target.getUniqueId()));

    }

    private String listOfFriends(UUID uuid) {
        StringBuilder builder = new StringBuilder();

        List<UUID> friendsList = this.friendManager.getFriends(uuid);

        if (friendsList.size() == 0) {
            builder.append(friendsConfig.emptyFriendList);
            return builder.toString();
        }

        builder.append(friendsConfig.friendListHeader);

        for (UUID friendUuid : friendsList) {
            builder.append(friendsConfig.friendListPlayer.replace("{player}", server.getOfflinePlayer(friendUuid).getName()));
        }

        return builder.toString();
    }
}
