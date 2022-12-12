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
    public Warnings warnings = new Warnings();

    @Contextual
    public static class Argument {
        public String missingPermission = "&cYou don't have permission to use this command! &7({permission})";
        public String invalidUsage = "&cBlad! Sprawdz poprawne uzycie komendy pod &f/znajomi pomoc&c.";
        public String playerOnly = "&cThis command can only be used by players!";
        public String playerNotFound = "&cPlayer not found: &7{player}&c.";
    }

    @Contextual
    public static class Friends {

        public String chatFormat = "&9[ZNAJOMI] &f{player} &8» &7{message}";
        public String profileNotFound = "&cNie znaleziono gracza o takiej nazwie!";
        public String yourProfileNotFound = "&cBrak twojego profilu w bazie!";
        public String inviteSent = "&aWyslano zaproszenie do znajomych graczowi &f{invited}";
        public String inviteReceived = "&aOtrzymales zaproszenie do znajomych od gracza &f{player}&a. By je akceptowac uzyj komendy &7/friend accept {player}";
        public String emptyFriendList = "&cbrak znajomych :(";
        public String emptyFriendListAdmin = "&cTen gracz nie posiada znajomych.";
        public String acceptedInvite = "&aZaakceptowales zaproszenie do znajomych od gracza &f{player}";
        public String yourInvitationHasBeenAccepted = "&f{player} &azaakceptowal twoje zaproszenie do znajomych.";
        public String alreadyFriend = "&cTen gracz jest juz na twojej liscie znajomych.";
        public String yourselfCommand = "&cNie mozesz uzyc tej komendy na samym Sobie.";
        public String inviteNotFound = "&cNie masz zaproszenia od tego gracza lub zaproszenie wygaslo!";
        public String playerIsNotYourFriend = "&cNie masz tego gracza na liscie znajomych";
        public String youKickedFriend = "&cGracz &f{player} &czostal wyrzucony z listy twoich znajomych!";
        public String friendKickedYou = "&cGracz &f{player} &cwyrzucil Cie z znajomych!";
        public String friendListHeader = "&3Twoi znajomi: ";
        public String friendListHeaderAdmin = "&3Znajomi gracza &f{player}&3: ";
        public String friendListPlayer = "&7{player}, ";
        public String alreadyReceivedInvite = "&cOtrzymales juz zaproszenie od tego gracza!";
        public String alreadySentInvite = "&cWyslales juz zaproszenie od temu graczowi!";
        public String inviteInstruction = "&aBy wyslac zaproszenie do znajomych uzyj komendy &7/friend zapros {nazwa_gracza}";
        public String configReloaded = "&aPomyslnie przeładowano konfiguracje!";
        public String inviteDenied = "&cOdrzucono zaproszenie od gracza &7{player}";
        public String youIgnoredPlayer = "&cNie bedziesz juz otrzymywal zaproszen od gracza &7{player}";
        public String youUnignoredPlayer = "&aPonownie mozesz otrzymywac zaproszenia od gracza &7{player}.";
        public String inviteExpired = "&cZaproszenie do znajomych od tego gracza wygaslo!";
        public List<String> helpCommand = List.of(
                " ",
                " &3Znajomi - komendy",
                " &7/znajomi zapros <gracz> &8- &fWysyla zaproszenie do znajomych",
                " &7/znajomi akceptuj <gracz> &8- &fAkceptuje zaproszenie do znajomych",
                " &7/znajomi odrzuc <gracz> &8- &fOdrzuca zaproszenie do znajomych",
                " &7/znajomi usun <gracz> &8- &fUsuwa gracza z listy znajomych",
                " &7/znajomi lista &8- &fPokazuje liste znajomych",
                " &7/znajomi reload &8- &fPrzeladowuje konfiguracje",
                " "
        );
    }

    @Contextual
    public static class Warnings {
        public String targetHasDisabledFriendRequests = "&cPlayer &7{player} &chas disabled invitations.";
        public String targetFriendsLimitIsReached = "&cPlayer &7{player} &chas reached the maximum amount of friends.";
    }

}
