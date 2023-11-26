package com.eternalcode.friends.config;

import net.dzikoysk.cdn.Cdn;
import net.dzikoysk.cdn.CdnFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConfigManager {

    private static final Cdn CDN = CdnFactory
        .createYamlLike()
        .getSettings()
        .build();
    private final ConfigBackupService configBackupService;
    private final Set<ReloadableConfig> configs = new HashSet<>();
    private final File dataFolder;

    public ConfigManager(File dataFolder, ConfigBackupService configBackupService) {
        this.dataFolder = dataFolder;
        this.configBackupService = configBackupService;
    }

    public <T extends ReloadableConfig> void load(T config) {
        CDN.load(config.resource(this.dataFolder), config)
            .orThrow(RuntimeException::new);

        CDN.render(config, config.resource(this.dataFolder))
            .orThrow(RuntimeException::new);

        this.configs.add(config);
    }

    public <T extends ReloadableConfig> void save(T config) {
        CDN.render(config, config.resource(this.dataFolder))
            .orThrow(RuntimeException::new);
    }

    public void reload() {
        configBackupService.createBackup();

        for (ReloadableConfig config : this.configs) {
            load(config);
        }
    }
}
