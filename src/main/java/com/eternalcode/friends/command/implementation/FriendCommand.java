package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.gui.MainGUI;
import com.eternalcode.friends.profile.Profile;
import com.eternalcode.friends.profile.ProfileManager;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Section(route = "friend", aliases = {"friends", "przyjaciele", "znajomi"})
public class FriendCommand {

    private final MainGUI mainGui;
    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;

    public FriendCommand(MainGUI mainGui, ProfileManager profileManager, NotificationAnnouncer announcer) {
        this.mainGui = mainGui;
        this.profileManager = profileManager;
        this.announcer = announcer;
    }

    @Execute(max = 0)
    void main(Player player) {
        this.mainGui.openInventory(player);
    }

    @Execute(min = 1, route = "invite", aliases = "zapros")
    public void invite(Player sender, @Arg @Name("player") Player target) {
        announcer.announceMessage(sender.getUniqueId(), "&aWyslano zaproszenie do znajomych graczowi &f" + target.getName());
        //TODO invite sending
    }

    @Execute(route = "list", aliases = "lista")
    public void list(Player sender) {
        if (profileManager.getProfileByUUID(sender.getUniqueId()) == null) {
            announcer.announceMessage(sender.getUniqueId(), "&cBlad! Nie znaleziono twojego profilu!");
            return;
        }

        Profile profile = profileManager.getProfileByUUID(sender.getUniqueId());
        StringBuilder builder = new StringBuilder();

        if (profile.getFriends().size() == 0) {
            for (UUID uuid : profile.getFriends()) {
                builder.append("&7" + Bukkit.getServer().getOfflinePlayer(uuid).getName() + "&7, ");
            }
        } else {
            builder.append("&cbrak :(");
        }

        announcer.announceMessage(sender.getUniqueId(), builder.toString());
    }


}
