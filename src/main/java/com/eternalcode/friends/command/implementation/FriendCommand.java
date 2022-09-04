package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
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
import org.bukkit.entity.Player;
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

    private String PROFILE_NOT_FOUND;
    private String YOUR_PROFILE_NOT_FOUND;
    private String INVITE_SENT;
    private String INVITE_RECEIVED;
    private String EMPTY_FRIEND_LIST;
    private String PLAYER_HAVE_EMPTY_FRIEND_LIST;
    private String YOU_ACCEPTED_FRIEND_INVITE;
    private String YOUR_INVITATION_HAS_BEEN_ACCEPTED;
    private String ALREADY_ON_YOUR_FRIEND_LIST;
    private String CANT_SEND_YOURSELF;
    private String INVITE_NOT_FOUND;
    private String PLAYER_IS_NOT_YOUR_FRIEND;
    private String YOU_KICKED_PLAYER_FROM_FRIENDS;
    private String PLAYER_KICKED_YOU_FROM_FRIENDS;
    private String FRIEND_LIST_HEADER;
    private String FRIEND_LIST_HEADER_ADMIN;
    private String FRIEND_LIST_ENTER;
    private String ALREADY_RECEIVED_INVITE;
    private String ALREADY_SENDT_INVITE;


    public FriendCommand(MainGUI mainGui, ProfileManager profileManager, NotificationAnnouncer announcer, InviteManager inviteManager, MessagesConfig messages) {
        this.mainGui = mainGui;
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.inviteManager = inviteManager;
        this.messages = messages;

        this.PROFILE_NOT_FOUND = messages.friends.profileNotFound;
        this.YOUR_PROFILE_NOT_FOUND = messages.friends.yourProfileNotFound;
        this.INVITE_SENT = messages.friends.inviteSent;
        this.INVITE_RECEIVED = messages.friends.inviteReceived;
        this.EMPTY_FRIEND_LIST = messages.friends.emptyFriendList;
        this.PLAYER_HAVE_EMPTY_FRIEND_LIST = messages.friends.emptyFriendListAdmin;
        this.YOU_ACCEPTED_FRIEND_INVITE = messages.friends.acceptedInvite;
        this.YOUR_INVITATION_HAS_BEEN_ACCEPTED = messages.friends.yourInvitationHasBeenAccepted;
        this.ALREADY_ON_YOUR_FRIEND_LIST = messages.friends.alreadyFriend;
        this.CANT_SEND_YOURSELF = messages.friends.yourselfCommand;
        this.INVITE_NOT_FOUND = messages.friends.inviteNotFound;
        this.PLAYER_IS_NOT_YOUR_FRIEND = messages.friends.playerIsNotYourFriend;
        this.YOU_KICKED_PLAYER_FROM_FRIENDS = messages.friends.youKickedFriend;
        this.PLAYER_KICKED_YOU_FROM_FRIENDS = messages.friends.friendKickedYou;
        this.FRIEND_LIST_HEADER = messages.friends.friendListHeader;
        this.FRIEND_LIST_HEADER_ADMIN = messages.friends.friendListHeaderAdmin;
        this.FRIEND_LIST_ENTER = messages.friends.friendListPlayer;
        this.ALREADY_RECEIVED_INVITE = messages.friends.alreadyReceivedInvite;
        this.ALREADY_SENDT_INVITE = messages.friends.alreadySentInvite;
    }

    @Execute(max = 0, required = 0)
    void main(Player player) {
        this.mainGui.openInventory(player);
    }

    @Execute(min = 1, route = "invite", aliases = "zapros", required = 1)
    public void invite(Player sender, @Arg @Name("player") Player target) {
        if(sender.getUniqueId().equals(target.getUniqueId())){
            announcer.announceMessage(sender.getUniqueId(), CANT_SEND_YOURSELF);
            return;
        }
        Optional<Profile> senderOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        if (senderOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), YOUR_PROFILE_NOT_FOUND);
            return;
        }
        Profile senderProfile = senderOptional.get();
        if(senderProfile.getFriends().contains(target.getUniqueId())){
            announcer.announceMessage(sender.getUniqueId(), ALREADY_ON_YOUR_FRIEND_LIST);
            return;
        }
        if(inviteManager.hasReceivedInvite(target.getUniqueId(), sender.getUniqueId()) || inviteManager.hasSendedInvite(target.getUniqueId(), sender.getUniqueId())){
            announcer.announceMessage(sender.getUniqueId(), ALREADY_RECEIVED_INVITE);
            return;
        }
        if(inviteManager.hasSendedInvite(sender.getUniqueId(), target.getUniqueId()) || inviteManager.hasReceivedInvite(sender.getUniqueId(), target.getUniqueId())){
            announcer.announceMessage(sender.getUniqueId(), ALREADY_SENDT_INVITE);
            return;
        }

        inviteManager.addInvite(sender.getUniqueId(), target.getUniqueId());
        announcer.announceMessage(sender.getUniqueId(), INVITE_SENT.replace("{invited}", target.getName()));
        announcer.announceMessage(target.getUniqueId(), INVITE_RECEIVED.replace("{player}", sender.getName()));
    }

    @Execute(route = "list", aliases = "lista", required = 0)
    public void list(Player sender) {
        Optional<Profile> playerOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        if (playerOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), YOUR_PROFILE_NOT_FOUND);
            return;
        }

        Profile profile = playerOptional.get();
        StringBuilder builder = new StringBuilder();

        if (profile.getFriends().size() == 0) {
            builder.append(EMPTY_FRIEND_LIST);
        } else {
            builder.append(FRIEND_LIST_HEADER);
            for (UUID uuid : profile.getFriends()) {
                builder.append(FRIEND_LIST_ENTER.replace("{player}", Bukkit.getServer().getOfflinePlayer(uuid).getName()));
            }
        }

        announcer.announceMessage(sender.getUniqueId(), builder.toString());
    }

    @Execute(route = "list", aliases = "lista", required = 1)
    @Permission("eternalfriends.admin")
    public void listAdmin(Player sender, @Arg @Name("player") Player player) {
        Optional<Profile> playerOptional = profileManager.getProfileByUUID(player.getUniqueId());
        if (playerOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), PROFILE_NOT_FOUND);
            return;
        }

        Profile profile = playerOptional.get();
        StringBuilder builder = new StringBuilder();

        if (profile.getFriends().size() == 0) {
            builder.append(PLAYER_HAVE_EMPTY_FRIEND_LIST);
        } else {
            builder.append(FRIEND_LIST_HEADER_ADMIN.replace("{player}", player.getName()));
            for (UUID uuid : profile.getFriends()) {
                builder.append(FRIEND_LIST_ENTER.replace("{player}", Bukkit.getServer().getOfflinePlayer(uuid).getName()));
            }
        }
        announcer.announceMessage(sender.getUniqueId(), builder.toString());
    }

    @Execute(route = "accept", aliases = "akceptuj", required = 1)
    public void accept(Player sender, @Arg @Name("player") Player player) {
        if(sender.getUniqueId().equals(player.getUniqueId())){
            announcer.announceMessage(sender.getUniqueId(), CANT_SEND_YOURSELF);
            return;
        }
        Optional<Profile> playerOptional = profileManager.getProfileByUUID(player.getUniqueId());
        Optional<Profile> senderOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        if (playerOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), PROFILE_NOT_FOUND);
            return;
        }
        if (senderOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), YOUR_PROFILE_NOT_FOUND);
            return;
        }
        Profile senderProfile = senderOptional.get();
        Profile playerProfile = playerOptional.get();

        UUID senderUuid = sender.getUniqueId();
        UUID playerUuid = player.getUniqueId();

        if(inviteManager.hasReceivedInvite(playerUuid, senderUuid)){
            senderProfile.addFriend(playerUuid);
            playerProfile.addFriend(senderUuid);

            inviteManager.removeInvite(playerUuid, senderUuid);

            announcer.announceMessage(senderUuid, YOU_ACCEPTED_FRIEND_INVITE.replace("{player}", player.getName()));
            announcer.announceMessage(playerUuid, YOUR_INVITATION_HAS_BEEN_ACCEPTED.replace("{player}", sender.getName()));
        }else {
            announcer.announceMessage(sender.getUniqueId(), INVITE_NOT_FOUND);
        }
    }

    @Execute(route = "kick", aliases = "wyrzuc", required = 1)
    public void kick(Player sender, @Arg @Name("player") Player player){
        if(sender.getUniqueId().equals(player.getUniqueId())){
            announcer.announceMessage(sender.getUniqueId(), CANT_SEND_YOURSELF);
            return;
        }
        Optional<Profile> playerOptional = profileManager.getProfileByUUID(player.getUniqueId());
        Optional<Profile> senderOptional = profileManager.getProfileByUUID(sender.getUniqueId());
        if (playerOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), PROFILE_NOT_FOUND);
            return;
        }
        if (senderOptional.isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), YOUR_PROFILE_NOT_FOUND);
            return;
        }
        Profile senderProfile = senderOptional.get();
        Profile playerProfile = playerOptional.get();

        if(!senderProfile.getFriends().contains(player.getUniqueId())){
            announcer.announceMessage(sender.getUniqueId(), PLAYER_IS_NOT_YOUR_FRIEND);
            return;
        }

        senderProfile.removeFriend(player.getUniqueId());
        playerProfile.removeFriend(sender.getUniqueId());
        announcer.announceMessage(sender.getUniqueId(), YOU_KICKED_PLAYER_FROM_FRIENDS.replace("{player}", player.getName()));
        announcer.announceMessage(player.getUniqueId(), PLAYER_KICKED_YOU_FROM_FRIENDS.replace("{player}", sender.getName()));
    }
}
