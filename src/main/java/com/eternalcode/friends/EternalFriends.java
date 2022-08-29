package com.eternalcode.friends;

import com.eternalcode.friends.command.implementation.FriendCommand;
import com.eternalcode.friends.config.ConfigManager;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.config.implementation.PluginConfig;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class EternalFriends extends JavaPlugin {

    private static EternalFriends instance;

    private ConfigManager configManager;
    private PluginConfig config;
    private MessagesConfig messages;

    private AudienceProvider audienceProvider;
    private MiniMessage miniMessage;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        instance = this;
        Server server = this.getServer();

        this.configManager = new ConfigManager(this.getDataFolder());

        this.config = new PluginConfig();
        this.messages = new MessagesConfig();


        this.configManager.load(this.config);
        this.configManager.load(this.messages);

        Metrics metrics = new Metrics(this, 16297);

        this.audienceProvider = BukkitAudiences.create(this);
        this.miniMessage = MiniMessage.builder().build();

        this.liteCommands = LiteBukkitFactory.builder(server, "friends")

                .commandInstance(new FriendCommand())

                .register();
    }

    @Override
    public void onDisable() {
        this.liteCommands.getPlatform().unregisterAll();
    }

    public static EternalFriends getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PluginConfig getPluginConfig() {
        return config;
    }

    public MessagesConfig getMessagesConfig() {
        return messages;
    }

    public AudienceProvider getAudienceProvider() {
        return audienceProvider;
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public LiteCommands<CommandSender> getLiteCommands() {
        return liteCommands;
    }
}
