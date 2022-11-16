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
import dev.rollczi.litecommands.command.section.Section;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

@Section(route = "friend", aliases = {"friends", "przyjaciele", "znajomi"})
public class FriendCommand {

    private final MainGui mainGui;
    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final InviteManager inviteManager;
    private final MessagesConfig messages;
    private final Server server;
    private final ConfigManager configManager;

    public FriendCommand(MainGui mainGui, ProfileManager profileManager, NotificationAnnouncer announcer, InviteManager inviteManager, MessagesConfig messages, Server server, ConfigManager configManager) {
        this.mainGui = mainGui;
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.inviteManager = inviteManager;
        this.messages = messages;
        this.server = server;
        this.configManager = configManager;
    }

    @Execute(max = 0)
    @Permission("eternalfriends.access")
    void main(Player player) {
        this.mainGui.openMainGui(player);
    }

    @Execute(min = 1, route = "invite", aliases = "zapros", required = 1)
    @Permission("eternalfriends.access.invite")
    public void invite(Player sender, @Arg @Name("player") Player target) {
        MessagesConfig.Friends friends = messages.friends;
        if (sender.equals(target)) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);
            return;
        }
        Optional<Profile> senderOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        if (senderOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourProfileNotFound);
            return;
        }
        Profile senderProfile = senderOptional.get();
        if (senderProfile.getFriends().contains(target.getUniqueId())) {
            announcer.announceMessage(sender.getUniqueId(), friends.alreadyFriend);
            return;
        }
        if (inviteManager.hasReceivedInvite(target.getUniqueId(), sender.getUniqueId()) || inviteManager.hasSendedInvite(target.getUniqueId(), sender.getUniqueId())) {
            announcer.announceMessage(sender.getUniqueId(), friends.alreadyReceivedInvite);
            return;
        }
        if (inviteManager.hasSendedInvite(sender.getUniqueId(), target.getUniqueId()) || inviteManager.hasReceivedInvite(sender.getUniqueId(), target.getUniqueId())) {
            announcer.announceMessage(sender.getUniqueId(), friends.alreadySentInvite);
            return;
        }

        inviteManager.addInvite(sender.getUniqueId(), target.getUniqueId());
        announcer.announceMessage(sender.getUniqueId(), friends.inviteSent.replace("{invited}", target.getName()));
        announcer.announceMessage(target.getUniqueId(), friends.inviteReceived.replace("{player}", sender.getName()));
    }

    @Execute(route = "list", aliases = "lista", required = 0)
    @Permission("eternalfriends.access.list")
    public void list(Player sender) {
        MessagesConfig.Friends friends = messages.friends;
        Optional<Profile> profileOptional = this.profileManager.getProfileByUUID(sender.getUniqueId());
        if (profileOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourProfileNotFound);
            return;
        }

        Profile profile = profileOptional.get();
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

        announcer.announceMessage(sender.getUniqueId(), builder.toString());
    }

    @Execute(route = "list", aliases = "lista", required = 1)
    @Permission("eternalfriends.admin.list")
    public void listAdmin(Player sender, @Arg @Name("player") Player player) {
        MessagesConfig.Friends friends = messages.friends;
        Optional<Profile> profileOptional = this.profileManager.getProfileByUUID(player.getUniqueId());
        if (profileOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), friends.profileNotFound);
            return;
        }

        Profile profile = profileOptional.get();
        StringBuilder builder = new StringBuilder();

        if (profile.getFriends().size() == 0) {
            builder.append(friends.emptyFriendListAdmin);
        }
        else {
            builder.append(friends.friendListHeaderAdmin.replace("{player}", player.getName()));
            for (UUID uuid : profile.getFriends()) {
                builder.append(friends.friendListPlayer.replace("{player}", server.getOfflinePlayer(uuid).getName()));
            }
        }
        this.announcer.announceMessage(sender.getUniqueId(), builder.toString());
    }

    @Execute(route = "accept", aliases = "akceptuj", required = 1)
    @Permission("eternalfriends.access.accept")
    public void accept(Player sender, @Arg @Name("player") Player player) {
        MessagesConfig.Friends friends = messages.friends;
        if (sender.equals(player)) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);
            return;
        }
        Optional<Profile> playerOptional = profileManager.getProfileByUUID(player.getUniqueId());
        Optional<Profile> senderOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        if (playerOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), friends.profileNotFound);
            return;
        }
        if (senderOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourProfileNotFound);
            return;
        }
        Profile senderProfile = senderOptional.get();
        Profile playerProfile = playerOptional.get();

        UUID senderUuid = sender.getUniqueId();
        UUID playerUuid = player.getUniqueId();

        if (inviteManager.hasReceivedInvite(playerUuid, senderUuid)) {
            senderProfile.addFriend(playerUuid);
            playerProfile.addFriend(senderUuid);

            inviteManager.removeInvite(playerUuid, senderUuid);

            announcer.announceMessage(senderUuid, friends.acceptedInvite.replace("{player}", player.getName()));
            announcer.announceMessage(playerUuid, friends.yourInvitationHasBeenAccepted.replace("{player}", sender.getName()));
        }
        else {
            announcer.announceMessage(sender.getUniqueId(), friends.inviteNotFound);
        }
    }

    @Execute(route = "kick", aliases = "wyrzuc", required = 1)
    @Permission("eternalfriends.access.kick")
    public void kick(Player sender, @Arg @Name("player") Player player){
        MessagesConfig.Friends friends = messages.friends;
        if (sender.equals(player)) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);
            return;
        }
        Optional<Profile> playerOptional = profileManager.getProfileByUUID(player.getUniqueId());
        Optional<Profile> senderOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        if (playerOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), friends.profileNotFound);
            return;
        }
        if (senderOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourProfileNotFound);
            return;
        }
        Profile senderProfile = senderOptional.get();
        Profile playerProfile = playerOptional.get();

        if (!senderProfile.getFriends().contains(player.getUniqueId())) {
            announcer.announceMessage(sender.getUniqueId(), friends.playerIsNotYourFriend);
            return;
        }

        senderProfile.removeFriend(player.getUniqueId());
        playerProfile.removeFriend(sender.getUniqueId());
        announcer.announceMessage(sender.getUniqueId(), friends.youKickedFriend.replace("{player}", player.getName()));
        announcer.announceMessage(player.getUniqueId(), friends.friendKickedYou.replace("{player}", sender.getName()));
    }

    @Execute(route = "reload", required = 0)
    @Permission("eternalfriends.admin.reload")
    public void reload(Player sender) {
        MessagesConfig.Friends friends = messages.friends;
        this.configManager.reload();
        this.announcer.announceMessage(sender.getUniqueId(), friends.configReloaded);
    }

}
