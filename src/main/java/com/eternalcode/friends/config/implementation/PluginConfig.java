package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.config.ReloadableConfig;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;

public class PluginConfig implements ReloadableConfig {

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "configuration.yml");
    }
}
