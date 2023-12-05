package com.eternalcode.friends.config.implementation;

import eu.okaeri.configs.OkaeriConfig;

import java.util.List;

public class MessagesConfig extends OkaeriConfig {

    public Argument argument = new Argument();
    public Friends friends = new Friends();

    public static class Argument extends OkaeriConfig {
        public String missingPermission = "<red>You do not have permission to use this command! <gray>({permission})";
        public String invalidUsage = "<red>Error! Check the correct usage of the command at <click:suggest_command:/friends help><white>/friends help <dark_gray>[click here]</click><red>.";
        public String playerOnly = "<red>This command can only be used by players!";
        public String playerNotFound = "<red>Player not found: <gray>{player}<red>.";
    }

    public static class Friends extends OkaeriConfig {
        public String chatFormat = "<blue>[FRIENDS] <white>{player} <dark_gray>» <gray>{message}";
        public String inviteSent = "<green>Invitation to friends sent to player <white>{invited}";
        public String inviteReceived = "<green>You have received a friend request from player <white>{player}<green>. To accept it, use the command <hover:show_text:\"/friends accept {player}\"><click:suggest_command:/friends accept {player}><gray>/friends accept {player} <dark_gray>[click]</click></hover>, <red>or to decline it, use the command <hover:show_text:\"/friends deny {player}\"><click:suggest_command:/friends deny {player}><gray>/friends deny {player} <dark_gray>[click]</click></hover>.";
        public String cannotSendInvite = "<red>You cannot send invitation to this player!";
        public String emptyFriendList = "<red>You have no friends :(";
        public String emptyFriendListAdmin = "<red>This player has no friends.";
        public String acceptedInvite = "<green>You accepted a friend request from player <white>{player}";
        public String yourInvitationHasBeenAccepted = "<white>{player} <green>accepted your friend request.";
        public String alreadyFriend = "<red>This player is already on your friends list.";
        public String yourselfCommand = "<red>You cannot use this command on yourself.";
        public String inviteNotFound = "<red>You have no friend request from this player or the request has expired!";
        public String playerIsNotYourFriend = "<red>This player is not on your friends list";
        public String youKickedFriend = "<red>Player <white>{player} <red>has been removed from your friends list!";
        public String friendKickedYou = "<red>Player <white>{player} <red>removed you from their friends list!";
        public String friendListHeader = "<dark_aqua>Your friends: ";
        public String friendListHeaderAdmin = "<dark_aqua>Friends of player <white>{player}<dark_aqua>: ";
        public String friendListPlayer = "<gray>{player}, ";
        public String alreadyReceivedInvite = "<red>You have already received a friend request from this player!";
        public String alreadySentInvite = "<red>You have already sent an invite to this player!";
        public String inviteInstruction = "<green>To send a friend request, use the command <gray>/friends invite {player_name}";
        public String configReloaded = "<green>Configuration successfully reloaded!";
        public String inviteDenied = "<red>You declined the friend request from player <gray>{player}";
        public String yourInvitationHasBeenDenied = "<red>Your invitation to <gray>{player} <red>has been denied.";
        public String youIgnoredPlayer = "<red>You will no longer receive friend requests from player <gray>{player}";
        public String playerNoLongerIgnored = "<green>You can now receive friend requests from player <gray>{player} again.";
        public String inviteExpired = "<red>The friend request from this player has expired!";
        public String friendJoined = "<blue>{friend} <green>has joined the server!";
        public List<String> helpCommand = List.of(
                " ",
                " <dark_aqua>Friends - Commands",
                " <gray>/friends <dark_gray>- <white>Displays friend menu",
                " <gray>/friends invite <player> <dark_gray>- <white>Sends a friend request",
                " <gray>/friends accept <player> <dark_gray>- <white>Accepts a friend request",
                " <gray>/friends deny <player> <dark_gray>- <white>Declines a friend request",
                " <gray>/friends remove <player> <dark_gray>- <white>Removes a friend from the list",
                " <gray>/friends ignore <player> <dark_gray>- <white>Ignores incoming friend requests from player or turns off ignoring",
                " <gray>/friends help <dark_gray>- <white>Displays available commands",
                " <gray>/friends list <dark_gray>- <white>Shows list of friends",
                " <gray>/friends reload <dark_gray>- <white>Reloads configuration",
                " "
        );
    }
}
