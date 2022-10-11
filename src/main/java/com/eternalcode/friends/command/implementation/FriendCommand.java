package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.gui.MainGUI;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.profile.Profile;
import com.eternalcode.friends.profile.ProfileManager;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.section.Section;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Section(route = "friend", aliases = {"friends", "przyjaciele", "znajomi"})
public class FriendCommand {

    private final MainGUI mainGui;
    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final InviteManager inviteManager;
    private final MessagesConfig messages;
    private final Server server;

    public FriendCommand(MainGUI mainGui, ProfileManager profileManager, NotificationAnnouncer announcer, InviteManager inviteManager, MessagesConfig messages, Server server) {
        this.mainGui = mainGui;
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.inviteManager = inviteManager;
        this.messages = messages;
        this.server = server;
    }

    @Execute(max = 0, required = 0)
    void main(Player player) {
        this.mainGui.openInventory(player);
    }

    @Execute(min = 1, route = "invite", aliases = "zapros", required = 1)
    public void invite(Player sender, @Arg @Name("player") Player target) {
        if (sender.getUniqueId().equals(target.getUniqueId())) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.yourselfCommand);
            return;
        }
        Optional<Profile> senderOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        if (senderOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.yourProfileNotFound);
            return;
        }
        Profile senderProfile = senderOptional.get();
        if (senderProfile.getFriends().contains(target.getUniqueId())) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.alreadyFriend);
            return;
        }
        if (inviteManager.hasReceivedInvite(target.getUniqueId(), sender.getUniqueId()) || inviteManager.hasSendedInvite(target.getUniqueId(), sender.getUniqueId())) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.alreadyReceivedInvite);
            return;
        }
        if (inviteManager.hasSendedInvite(sender.getUniqueId(), target.getUniqueId()) || inviteManager.hasReceivedInvite(sender.getUniqueId(), target.getUniqueId())) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.alreadySentInvite);
            return;
        }

        inviteManager.addInvite(sender.getUniqueId(), target.getUniqueId());
        announcer.announceMessage(sender.getUniqueId(), messages.friends.inviteSent.replace("{invited}", target.getName()));
        announcer.announceMessage(target.getUniqueId(), messages.friends.inviteReceived.replace("{player}", sender.getName()));
    }

    @Execute(route = "list", aliases = "lista", required = 0)
    public void list(Player sender) {
        Optional<Profile> profileOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        if (profileOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.yourProfileNotFound);
            return;
        }

        Profile profile = profileOptional.get();
        StringBuilder builder = new StringBuilder();

        if (profile.getFriends().size() == 0) {
            builder.append(messages.friends.emptyFriendList);
        }
        else {
            builder.append(messages.friends.friendListHeader);
            for (UUID uuid : profile.getFriends()) {
                builder.append(messages.friends.friendListPlayer.replace("{player}", server.getOfflinePlayer(uuid).getName()));
            }
        }

        announcer.announceMessage(sender.getUniqueId(), builder.toString());
    }

    @Execute(route = "list", aliases = "lista", required = 1)
    @Permission("eternalfriends.admin")
    public void listAdmin(Player sender, @Arg @Name("player") Player player) {
        Optional<Profile> profileOptional = profileManager.getProfileByUUID(player.getUniqueId());
        if (profileOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.profileNotFound);
            return;
        }

        Profile profile = profileOptional.get();
        StringBuilder builder = new StringBuilder();

        if (profile.getFriends().size() == 0) {
            builder.append(messages.friends.emptyFriendListAdmin);
        }
        else {
            builder.append(messages.friends.friendListHeaderAdmin.replace("{player}", player.getName()));
            for (UUID uuid : profile.getFriends()) {
                builder.append(messages.friends.emptyFriendList.replace("{player}", server.getOfflinePlayer(uuid).getName()));
            }
        }
        announcer.announceMessage(sender.getUniqueId(), builder.toString());
    }

    @Execute(route = "accept", aliases = "akceptuj", required = 1)
    public void accept(Player sender, @Arg @Name("player") Player player) {
        if (sender.getUniqueId().equals(player.getUniqueId())) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.yourselfCommand);
            return;
        }
        Optional<Profile> playerOptional = profileManager.getProfileByUUID(player.getUniqueId());
        Optional<Profile> senderOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        if (playerOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.profileNotFound);
            return;
        }
        if (senderOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.yourProfileNotFound);
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

            announcer.announceMessage(senderUuid, messages.friends.acceptedInvite.replace("{player}", player.getName()));
            announcer.announceMessage(playerUuid, messages.friends.yourInvitationHasBeenAccepted.replace("{player}", sender.getName()));
        }
        else {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.inviteNotFound);
        }
    }

    @Execute(route = "kick", aliases = "wyrzuc", required = 1)
    public void kick(Player sender, @Arg @Name("player") Player player){
        if (sender.getUniqueId().equals(player.getUniqueId())) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.yourselfCommand);
            return;
        }
        Optional<Profile> playerOptional = profileManager.getProfileByUUID(player.getUniqueId());
        Optional<Profile> senderOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        if (playerOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.profileNotFound);
            return;
        }
        if (senderOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.yourProfileNotFound);
            return;
        }
        Profile senderProfile = senderOptional.get();
        Profile playerProfile = playerOptional.get();

        if (!senderProfile.getFriends().contains(player.getUniqueId())) {
            announcer.announceMessage(sender.getUniqueId(), messages.friends.playerIsNotYourFriend);
            return;
        }

        senderProfile.removeFriend(player.getUniqueId());
        playerProfile.removeFriend(sender.getUniqueId());
        announcer.announceMessage(sender.getUniqueId(), messages.friends.youKickedFriend.replace("{player}", player.getName()));
        announcer.announceMessage(player.getUniqueId(), messages.friends.friendKickedYou.replace("{player}", sender.getName()));
    }
}
