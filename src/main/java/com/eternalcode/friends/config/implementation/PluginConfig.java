package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.config.ReloadableConfig;
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

    @Description("# Color of player's nametag displayed to his friends")
    public ChatColor friendColor = ChatColor.BLUE;

    @Description("# Configuration of command and subcommands names")
    public Command friendCommand = new Command();

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


}
