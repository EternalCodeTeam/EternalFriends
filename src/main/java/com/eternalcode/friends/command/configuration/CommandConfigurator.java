package com.eternalcode.friends.command.configuration;

import com.eternalcode.friends.config.implementation.PluginConfig;
import dev.rollczi.litecommands.factory.CommandEditor;

public class CommandConfigurator implements CommandEditor {

    private final PluginConfig config;

    public CommandConfigurator(PluginConfig config) {
        this.config = config;
    }

    @Override
    public State edit(State state) {
        PluginConfig.SubCommand subCommandsCfg = config.friendCommand.subCommands;

        return state
                .name(config.friendCommand.main)
                .editChild("kick", child -> child.name(subCommandsCfg.kick))
                .editChild("accept", child -> child.name(subCommandsCfg.accept))
                .editChild("deny", child -> child.name(subCommandsCfg.deny))
                .editChild("list", child -> child.name(subCommandsCfg.list))
                .editChild("help", child -> child.name(subCommandsCfg.help))
                .editChild("ignore", child -> child.name(subCommandsCfg.ignore))
                .editChild("invite", child -> child.name(subCommandsCfg.invite));
    }
}
