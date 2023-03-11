package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.config.ReloadableConfig;
import com.eternalcode.friends.database.DatabaseType;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import org.bukkit.ChatColor;

import java.io.File;

public class PluginConfig implements ReloadableConfig {

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "configuration.yml");
    }

    @Description("# Configuration of Database")
    public Database database = new Database();

    @Description("# Color of player's nametag displayed to his friends")
    public ChatColor friendColor = ChatColor.BLUE;

    @Description("# Configuration of command and subcommands names")
    public Command friendCommand = new Command();

    @Description("# Time in seconds after which invite will expire")
    public int inviteExpirationTime = 60;

    @Contextual
    public static class Command {
        public String main = "friends";
        public SubCommand subCommands = new SubCommand();
    }

    @Contextual
    public static class SubCommand {
        public String invite = "invite";
        public String accept = "accept";
        public String deny = "deny";
        public String kick = "kick";
        public String list = "list";
        public String help = "help";
        public String ignore = "ignore";
        public String reload = "reload";
    }

    @Contextual
    public static class Database {
        public DatabaseType databaseType = DatabaseType.MYSQL;
        public String host = "localhost";
        public int port = 3306;
        public String username = "admin";
        public String password = "password";
        public String database = "server";
    }
}
