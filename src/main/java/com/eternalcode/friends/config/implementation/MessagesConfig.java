package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.config.ReloadableConfig;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;

public class MessagesConfig implements ReloadableConfig {

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "messages.yml");
    }

    public Argument argument = new Argument();

    @Contextual
    public static class Argument {
        public String missingPermission = "&cYou don't have permission to use this command! &7({permission})";
        public String correctUsage = "&cCorrect usage: &7{usage}&c.";
        public String playerOnly = "&cThis command can only be used by players!";
        public String playerNotFound = "&cPlayer not found: &7{player}&c.";
    }

    @Contextual
    public static class Friends {

        @Contextual
        public static class Warnings {
            public String targetHasDisabledFriendRequests = "&cPlayer &7{player} &chas disabled invitations.";
            public String targetFriendsLimitIsReached = "&cPlayer &7{player} &chas reached the maximum amount of friends.";
        }
    }

}
