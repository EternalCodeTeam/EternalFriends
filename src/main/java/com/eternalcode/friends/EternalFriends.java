package com.eternalcode.friends;

import com.eternalcode.friends.command.handler.InvalidUsage;
import com.eternalcode.friends.command.handler.PermissionMessage;
import com.eternalcode.friends.command.implementation.FriendCommand;
import com.eternalcode.friends.config.ConfigManager;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.config.implementation.PluginConfig;
import com.eternalcode.friends.gui.MainGUI;
import com.eternalcode.friends.profile.ProfileManager;
import com.eternalcode.friends.profile.ProfileRepositoryImpl;
import com.eternalcode.friends.util.legacy.LegacyColorProcessor;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class EternalFriends extends JavaPlugin {

    private static EternalFriends instance;

    private NotificationAnnouncer announcer;

    private ConfigManager configManager;
    private PluginConfig config;
    private MessagesConfig messages;

    private MainGUI mainGui;

    private ProfileManager profileManager;

    private AudienceProvider audienceProvider;
    private MiniMessage miniMessage;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        instance = this;
        Server server = this.getServer();

        this.audienceProvider = BukkitAudiences.create(this);
        this.miniMessage = MiniMessage.builder().postProcessor(new LegacyColorProcessor()).build();

        this.announcer = new NotificationAnnouncer(this.audienceProvider, this.miniMessage);

        this.configManager = new ConfigManager(this.getDataFolder());

        this.config = new PluginConfig();
        this.messages = new MessagesConfig();

        this.configManager.load(this.config);
        this.configManager.load(this.messages);

        this.mainGui = new MainGUI(this.miniMessage);

        this.profileManager = new ProfileManager(new ProfileRepositoryImpl());

        Metrics metrics = new Metrics(this, 16297);

        this.liteCommands = LiteBukkitFactory.builder(server, "friends")
                .argument(Player.class, new BukkitPlayerArgument(server, this.messages.argument.playerNotFound))

                .invalidUsageHandler(new InvalidUsage(this.messages, this.announcer))
                .permissionHandler(new PermissionMessage(this.messages, this.announcer))

                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>(this.messages.argument.playerOnly))

                .commandInstance(new FriendCommand(this.mainGui, this.profileManager, this.announcer))

                .register();
    }

    @Override
    public void onDisable() {
        this.liteCommands.getPlatform().unregisterAll();
    }

    public static EternalFriends getInstance() {
        return instance;
    }

    public NotificationAnnouncer getAnnouncer() {
        return this.announcer;
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

    public MainGUI getMainGui() {
        return mainGui;
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
