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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

@Section(route = "friends")
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

    @Execute(required = 0)
    @Permission("eternalfriends.access")
    void main(Player player) {
        this.mainGui.openMainGui(player);
    }

    @Execute(route = "invite", required = 1)
    @Permission("eternalfriends.access.invite")
    public void invite(Player sender, @Arg @Name("player") Player target) {
        MessagesConfig.Friends friends = messages.friends;
        if (sender.equals(target)) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);
            return;
        }
        Optional<Profile> senderOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        Optional<Profile> targetOptional = profileManager.getProfileByUUID(target.getUniqueId());
        if (targetOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), friends.profileNotFound);
            return;
        }
        if (senderOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourProfileNotFound);
            return;
        }
        Profile senderProfile = senderOptional.get();
        Profile targetProfile = targetOptional.get();
        if (targetProfile.isIgnoredPlayer(sender.getUniqueId())) {
            return;
        }
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

    @Execute(route = "list")
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

    @Execute(route = "list", required = 1)
    @Permission("eternalfriends.admin.list")
    public void listAdmin(CommandSender sender, @Arg @Name("player") Player player) {
        MessagesConfig.Friends friends = messages.friends;
        Optional<Profile> profileOptional = this.profileManager.getProfileByUUID(player.getUniqueId());
        if (profileOptional.isEmpty()) {
            announcer.announceMessage(sender, friends.profileNotFound);
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
        this.announcer.announceMessage(sender, builder.toString());
    }

    @Execute(route = "accept", required = 1)
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

    @Execute(route = "deny", required = 1)
    @Permission("eternalfriends.access.deny")
    public void deny(Player sender, @Arg @Name("player") Player player) {
        MessagesConfig.Friends friends = messages.friends;
        if (sender.equals(player)) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);
            return;
        }
        profileManager.getProfileByUUID(player.getUniqueId()).ifPresent(playerProfile -> {
            profileManager.getProfileByUUID(sender.getUniqueId()).ifPresent(senderProfile -> {
                UUID senderUuid = sender.getUniqueId();
                UUID playerUuid = player.getUniqueId();
                if (inviteManager.hasReceivedInvite(playerUuid, senderUuid)) {
                    inviteManager.removeInvite(playerUuid, senderUuid);
                    announcer.announceMessage(senderUuid, friends.inviteDenied.replace("{player}", player.getName()));
                }
                else {
                    announcer.announceMessage(sender.getUniqueId(), friends.inviteNotFound);
                }
            });
        });
    }

    @Execute(route = "kick", required = 1)
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

    @Execute(route = "ignore", required = 1)
    @Permission("eternalfriends.access.ignore")
    public void ignore(Player sender, @Arg @Name("player") Player player) {
        MessagesConfig.Friends friends = messages.friends;
        if (sender.equals(player)) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);
            return;
        }
        Optional<Profile> senderOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        if (senderOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), friends.yourProfileNotFound);
            return;
        }
        Profile senderProfile = senderOptional.get();

        if (senderProfile.isIgnoredPlayer(player.getUniqueId())) {
            senderProfile.removeIgnoredPlayer(player.getUniqueId());
            announcer.announceMessage(sender.getUniqueId(), friends.youUnignoredPlayer.replace("{player}", player.getName()));
            return;
        }

        senderProfile.addIgnoredPlayer(player.getUniqueId());
        announcer.announceMessage(sender.getUniqueId(), friends.youIgnoredPlayer.replace("{player}", player.getName()));
    }

    @Execute(route = "help")
    @Permission("eternalfriends.access.help")
    public void help(CommandSender sender) {
        for (String message : messages.friends.helpCommand) {
            announcer.announceMessage(sender, message);
        }
    }

    @Execute(route = "reload")
    @Permission("eternalfriends.admin.reload")
    public void reload(CommandSender sender) {
        MessagesConfig.Friends friends = messages.friends;
        this.configManager.reload();
        this.announcer.announceMessage(sender, friends.configReloaded);
    }

}
