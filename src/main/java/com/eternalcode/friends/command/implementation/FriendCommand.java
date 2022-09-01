package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.gui.MainGUI;
import com.eternalcode.friends.profile.Profile;
import com.eternalcode.friends.profile.ProfileManager;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.section.Section;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Section(route = "friend", aliases = {"friends", "przyjaciele", "znajomi"})
public class FriendCommand {

    private final MainGUI mainGui;
    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;

    private final String PROFILE_NOT_FOUND = "&cNie znaleziono gracza o takiej nazwie!";
    private final String YOUR_PROFILE_NOT_FOUND = "&cBrak twojego profilu w bazie!";
    private final String INVITE_SENT = "&aWyslano zaproszenie do znajomych graczowi &f{invited}";
    private final String INVITE_RECEIVED = "&aOtrzymales zaproszenie do znajomych od gracza &f{player}&a. By je akceptowac uzyj komendy &7/friend accept {player}";
    private final String EMPTY_FRIEND_LIST = "&cbrak znajomych :(";
    private final String PLAYER_HAVE_EMPTY_FRIEND_LIST = "&cTen gracz nie posiada znajomych.";
    private final String YOU_ACCEPTED_FRIEND_INVITE = "&aZaakceptowales zaproszenie do znajomych od gracza &f{player}";
    private final String YOUR_INVITATION_HAS_BEEN_ACCEPTED = "&f{player} &azaakceptowal twoje zaproszenie do znajomych.";

    public FriendCommand(MainGUI mainGui, ProfileManager profileManager, NotificationAnnouncer announcer) {
        this.mainGui = mainGui;
        this.profileManager = profileManager;
        this.announcer = announcer;
    }

    @Execute(max = 0, required = 0)
    void main(Player player) {
        this.mainGui.openInventory(player);
    }

    @Execute(min = 1, route = "invite", aliases = "zapros", required = 1)
    public void invite(Player sender, @Arg @Name("player") Player target) {
        if(sender.getUniqueId().toString().equalsIgnoreCase(target.getUniqueId().toString())){
            return;
        }
        if (profileManager.getProfileByUUID(target.getUniqueId()).isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), PROFILE_NOT_FOUND);
            return;
        }
        if (profileManager.getProfileByUUID(sender.getUniqueId()).isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), YOUR_PROFILE_NOT_FOUND);
            return;
        }
        Profile senderProfile = profileManager.getProfileByUUID(sender.getUniqueId()).get();
        Profile targetProfile = profileManager.getProfileByUUID(target.getUniqueId()).get();
        senderProfile.sendInviteTo(target.getUniqueId());
        targetProfile.sendInviteTo(sender.getUniqueId());
        announcer.announceMessage(sender.getUniqueId(), INVITE_SENT.replace("{invited}", target.getName()));
        announcer.announceMessage(target.getUniqueId(), INVITE_RECEIVED.replace("{player}", sender.getName()));
    }

    @Execute(route = "list", aliases = "lista", required = 0)
    public void list(Player sender) {
        if (profileManager.getProfileByUUID(sender.getUniqueId()).isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), YOUR_PROFILE_NOT_FOUND);
            return;
        }

        Profile profile = profileManager.getProfileByUUID(sender.getUniqueId()).get();
        StringBuilder builder = new StringBuilder();

        if (profile.getFriends().size() == 0) {
            builder.append(EMPTY_FRIEND_LIST);
        } else {
            for (UUID uuid : profile.getFriends()) {
                builder.append("&7" + Bukkit.getServer().getOfflinePlayer(uuid).getName() + "&7, ");
            }
        }

        announcer.announceMessage(sender.getUniqueId(), builder.toString());
    }

    @Execute(route = "list", aliases = "lista", required = 1)
    @Permission("eternalfriends.admin")
    public void listAdmin(Player sender, @Arg @Name("player") Player player) {
        if (profileManager.getProfileByUUID(player.getUniqueId()).isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), PROFILE_NOT_FOUND);
            return;
        }

        Profile profile = profileManager.getProfileByUUID(player.getUniqueId()).get();
        StringBuilder builder = new StringBuilder();

        if (profile.getFriends().size() == 0) {
            builder.append(PLAYER_HAVE_EMPTY_FRIEND_LIST);
        } else {
            for (UUID uuid : profile.getFriends()) {
                builder.append("&7" + Bukkit.getServer().getOfflinePlayer(uuid).getName() + "&7, ");
            }
        }
        announcer.announceMessage(sender.getUniqueId(), builder.toString());
    }

    @Execute(route = "accept", aliases = "akceptuj", required = 1)
    public void accept(Player sender, @Arg @Name("player") Player player) {
        if(sender.getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())){
            return;
        }
        if (profileManager.getProfileByUUID(player.getUniqueId()).isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), PROFILE_NOT_FOUND);
            return;
        }
        if (profileManager.getProfileByUUID(sender.getUniqueId()).isEmpty()) {
            announcer.announceMessage(sender.getUniqueId(), YOUR_PROFILE_NOT_FOUND);
            return;
        }
        Profile senderProfile = profileManager.getProfileByUUID(sender.getUniqueId()).get();
        Profile playerProfile = profileManager.getProfileByUUID(player.getUniqueId()).get();

        List<UUID> receivedInvites = senderProfile.getReceivedInvites();
        if(receivedInvites.size() > 0){
            for(UUID uuid : receivedInvites){
                if(!uuid.toString().equalsIgnoreCase(player.getUniqueId().toString()))continue;

                UUID senderUuid = sender.getUniqueId();

                senderProfile.addFriend(uuid);
                playerProfile.addFriend(senderUuid);

                senderProfile.removeInviteFrom(uuid);
                playerProfile.removeInviteFrom(senderUuid);

                announcer.announceMessage(senderUuid, YOU_ACCEPTED_FRIEND_INVITE.replace("{player}", player.getName()));
                announcer.announceMessage(senderUuid, YOUR_INVITATION_HAS_BEEN_ACCEPTED.replace("{player}", player.getName()));
            }
        }

    }

    //TODO komenda /friend kick
}
