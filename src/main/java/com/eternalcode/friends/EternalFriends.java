package com.eternalcode.friends;

import com.eternalcode.friends.command.configuration.CommandConfigurator;
import com.eternalcode.friends.command.handler.InvalidUsage;
import com.eternalcode.friends.command.handler.PermissionMessage;
import com.eternalcode.friends.command.implementation.FriendCommand;
import com.eternalcode.friends.config.ConfigManager;
import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.config.implementation.PluginConfig;
import com.eternalcode.friends.gui.MainGui;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.listener.ChatEventListener;
import com.eternalcode.friends.listener.DamageEventListener;
import com.eternalcode.friends.profile.ProfileJoinListener;
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

import java.util.stream.Stream;

public class EternalFriends extends JavaPlugin {

    private static EternalFriends instance;

    private NotificationAnnouncer announcer;

    private ConfigManager configManager;
    private PluginConfig config;
    private MessagesConfig messages;

    private GuiConfig guiConfig;

    private MainGui mainGui;


    private InviteManager inviteManager;
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
        this.guiConfig = new GuiConfig();

        this.configManager.load(this.config);
        this.configManager.load(this.messages);
        this.configManager.load(this.guiConfig);

        this.inviteManager = new InviteManager(this);

        this.profileManager = new ProfileManager(new ProfileRepositoryImpl());

        this.mainGui = new MainGui(this.miniMessage, this.guiConfig, this, this.profileManager, this.announcer, this.messages, this.inviteManager);

        Stream.of(
                new ProfileJoinListener(profileManager),
                new DamageEventListener(this.profileManager),
                new ChatEventListener(this.profileManager, this.announcer, this.messages)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));

        Metrics metrics = new Metrics(this, 16297);

        this.liteCommands = LiteBukkitFactory.builder(server, "EternalFriends")
                .argument(Player.class, new BukkitPlayerArgument<>(server, this.messages.argument.playerNotFound))

                .invalidUsageHandler(new InvalidUsage(this.messages, this.announcer))
                .permissionHandler(new PermissionMessage(this.messages, this.announcer))

                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>(this.messages.argument.playerOnly))

                .commandInstance(new FriendCommand(this.mainGui, this.profileManager, this.announcer, this.inviteManager, this.messages, this.getServer(), this.configManager))

                .commandEditor("friends", new CommandConfigurator(this.config))

                .register();

    }

    @Override
    public void onDisable() {
        this.liteCommands.getPlatform().unregisterAll();
    }
}
