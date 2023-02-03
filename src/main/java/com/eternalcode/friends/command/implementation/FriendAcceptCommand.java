package com.eternalcode.friends.command.implementation;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.packet.WrapperPlayServerScoreboardTeam;
import com.eternalcode.friends.profile.Profile;
import com.eternalcode.friends.profile.ProfileManager;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.nio.Buffer;
import java.util.Optional;
import java.util.UUID;

@Route(name = "friends")
public class FriendAcceptCommand {

    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final InviteManager inviteManager;
    private final MessagesConfig messages;
    private final ProtocolManager protocolManager;

    public FriendAcceptCommand(ProfileManager profileManager, NotificationAnnouncer announcer, InviteManager inviteManager, MessagesConfig messages, ProtocolManager protocolManager) {
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.inviteManager = inviteManager;
        this.messages = messages;
        this.protocolManager = protocolManager;
    }

    @Execute(route = "accept", required = 1)
    @Permission("eternalfriends.access.accept")
    public void accept(Player sender, @Arg @Name("player") Player target) {
        MessagesConfig.Friends friends = this.messages.friends;

        if (sender.equals(target)) {
            this.announcer.announceMessage(sender.getUniqueId(), friends.yourselfCommand);

            return;
        }

        Optional<Profile> targetOptional = this.profileManager.getProfileByUUID(target.getUniqueId());
        Optional<Profile> senderOptional = this.profileManager.getProfileByUUID(sender.getUniqueId());

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

        UUID senderUuid = sender.getUniqueId();
        UUID targetUuid = target.getUniqueId();

        if (this.inviteManager.hasReceivedInvite(targetUuid, senderUuid)) {
            if (this.inviteManager.isInviteExpired(targetUuid, senderUuid)) {
                this.inviteManager.removeInvite(targetUuid, senderUuid);
                this.announcer.announceMessage(sender.getUniqueId(), friends.inviteExpired);

                return;
            }

            senderProfile.addFriend(targetUuid);
            targetProfile.addFriend(senderUuid);

            //refreshing players color name above head
            WrapperPlayServerScoreboardTeam senderPacket = new WrapperPlayServerScoreboardTeam()
                    .setName(sender.getName())
                    .setMode(2)
                    .setColor(ChatColor.BLUE);
            this.protocolManager.sendServerPacket(target, senderPacket.getHandle());

            WrapperPlayServerScoreboardTeam targetPacket = new WrapperPlayServerScoreboardTeam()
                    .setName(target.getName())
                    .setMode(2)
                    .setColor(ChatColor.BLUE);
            this.protocolManager.sendServerPacket(sender, targetPacket.getHandle());
            //

            this.inviteManager.removeInvite(targetUuid, senderUuid);

            this.announcer.announceMessage(senderUuid, friends.acceptedInvite.replace("{player}", target.getName()));
            this.announcer.announceMessage(targetUuid, friends.yourInvitationHasBeenAccepted.replace("{player}", sender.getName()));

            return;
        }
        this.announcer.announceMessage(sender.getUniqueId(), friends.inviteNotFound);
    }
}
