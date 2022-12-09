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
        if (this.config.commandLang.equalsIgnoreCase("pl")) {
            return state
                    .name("znajomi")
                    .editChild("kick", child -> child.name("wyrzuc"))
                    .editChild("accept", child -> child.name("akceptuj"))
                    .editChild("deny", child -> child.name("odrzuc"))
                    .editChild("list", child -> child.name("lista"))
                    .editChild("list", child -> child.name("lista"))
                    .editChild("help", child -> child.name("pomoc"))
                    .editChild("invite", child -> child.name("zapros"));

        }
        return state;

    }
}
