package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.database.DatabaseType;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.*;
import org.bukkit.ChatColor;

@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
@Header("# ")
@Header("# EternalFriends configuration file")
@Header("# Permissions:")
@Header("# - eternalfriends.access.all - gives access to all basic command functionality")
@Header("# - eternalfriends.admin.all - gives access to all administrative commands")
@Header("# ")
public class PluginConfig extends OkaeriConfig {

    @Comment("# Configuration of Database")
    public Database database = new Database();
    @Comment("# Color of player's name tag displayed to his friends")
    public ChatColor friendColor = ChatColor.BLUE;
    @Comment("# Does server should message displayed to player when his friend joins the server?")
    public boolean announceFriendJoin = true;
    @Comment("# Configuration of command and subcommands names")
    public Command friendCommand = new Command();
    @Comment("# Time in seconds after which invite will expire")
    public int inviteExpirationTime = 60;

    public static class Command extends OkaeriConfig {
        public String main = "friends";
        public SubCommand subCommands = new SubCommand();
    }

    public static class SubCommand extends OkaeriConfig {
        public String invite = "invite";
        public String accept = "accept";
        public String deny = "deny";
        public String kick = "kick";
        public String list = "list";
        public String help = "help";
        public String ignore = "ignore";
    }

    public static class Database extends OkaeriConfig {
        public DatabaseType databaseType = DatabaseType.SQLITE;
        public String host = "localhost";
        public int port = 3306;
        public String username = "root";
        public String password = "Us3$tr0ngP@$$w0rd!@#";
        public String database = "eternalfriends";
    }
}
