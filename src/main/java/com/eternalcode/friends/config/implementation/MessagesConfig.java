package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.config.ReloadableConfig;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;
import java.util.List;

public class MessagesConfig implements ReloadableConfig {

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "messages.yml");
    }

    public Argument argument = new Argument();
    public Friends friends = new Friends();

    @Contextual
    public static class Argument {
        public String missingPermission = "&cYou do not have permission to use this command! &7({permission})";
        public String invalidUsage = "&cError! Check the correct usage of the command at <click:suggest_command:/friends help>&f/friends help &8[click here]</click>&c.";
        public String playerOnly = "&cThis command can only be used by players!";
        public String playerNotFound = "&cPlayer not found: &7{player}&c.";
    }

    @Contextual
    public static class Friends {
        public String chatFormat = "&9[FRIENDS] &f{player} &8» &7{message}";
        public String inviteSent = "&aInvitation to friends sent to player &f{invited}";
        public String inviteReceived = "&aYou have received a friend request from player &f{player}&a. To accept it, use the command <hover:show_text:\"/friends accept {player}\"><click:suggest_command:/friends accept {player}>&7/friends accept {player} &8[click]</click></hover>, &cor to decline it, use the command <hover:show_text:\"/friends deny {player}\"><click:suggest_command:/friends deny {player}>&7/friends deny {player} &8[click]</click></hover>.";
        public String cannotSendInvite = "&cYou cannot send invitation to this player!";
        public String emptyFriendList = "&cYou have no friends :(";
        public String emptyFriendListAdmin = "&cThis player has no friends.";
        public String acceptedInvite = "&aYou accepted a friend request from player &f{player}";
        public String yourInvitationHasBeenAccepted = "&f{player} &aaccepted your friend request.";
        public String alreadyFriend = "&cThis player is already on your friends list.";
        public String yourselfCommand = "&cYou cannot use this command on yourself.";
        public String inviteNotFound = "&cYou have no friend request from this player or the request has expired!";
        public String playerIsNotYourFriend = "&cThis player is not on your friends list";
        public String youKickedFriend = "&cPlayer &f{player} &chas been removed from your friends list!";
        public String friendKickedYou = "&cPlayer &f{player} &cremoved you from their friends list!";
        public String friendListHeader = "&3Your friends: ";
        public String friendListHeaderAdmin = "&3Friends of player &f{player}&3: ";
        public String friendListPlayer = "&7{player}, ";
        public String alreadyReceivedInvite = "&cYou have already received a friend request from this player!";
        public String alreadySentInvite = "&cYou have already sent an invite to this player!";
        public String inviteInstruction = "&aTo send a friend request, use the command &7/friends invite {player_name}";
        public String configReloaded = "&aConfiguration successfully reloaded!";
        public String inviteDenied = "&cYou declined the friend request from player &7{player}";
        public String yourInvitationHasBeenDenied = "&cYour invitation to &7{player} &chas been denied.";
        public String youIgnoredPlayer = "&cYou will no longer receive friend requests from player &7{player}";
        public String playerIsNoLongerIgnored = "&aYou can now receive friend requests from player &7{player} again.";
        public String inviteExpired = "&cThe friend request from this player has expired!";
        public String friendJoined = "&9{friend} &ahas joined the server!";
        public List<String> helpCommand = List.of(
                " ",
                " &3Friends - Commands",
                " &7/friends &8- &fDisplays friend menu",
                " &7/friends invite <player> &8- &fSends a friend request",
                " &7/friends accept <player> &8- &fAccepts a friend request",
                " &7/friends deny <player> &8- &fDeclines a friend request",
                " &7/friends remove <player> &8- &fRemoves a friend from the list",
                " &7/friends ignore <player> &8- &fIgnores incoming friend requests from player or turns off ignoring",
                " &7/friends help &8- &fDisplays available commands",
                " &7/friends list &8- &fShows list of friends",
                " &7/friends reload &8- &fReloads configuration",
                " "
        );
    }
}
