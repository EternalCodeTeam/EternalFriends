package com.eternalcode.friends;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.eternalcode.friends.command.configuration.CommandConfigurator;
import com.eternalcode.friends.command.handler.InvalidUsage;
import com.eternalcode.friends.command.handler.PermissionMessage;
import com.eternalcode.friends.command.implementation.FriendAcceptCommand;
import com.eternalcode.friends.command.implementation.FriendCommand;
import com.eternalcode.friends.command.implementation.FriendDenyCommand;
import com.eternalcode.friends.command.implementation.FriendHelpCommand;
import com.eternalcode.friends.command.implementation.FriendIgnoreCommand;
import com.eternalcode.friends.command.implementation.FriendInviteCommand;
import com.eternalcode.friends.command.implementation.FriendKickCommand;
import com.eternalcode.friends.command.implementation.FriendListCommand;
import com.eternalcode.friends.command.implementation.FriendReloadCommand;
import com.eternalcode.friends.config.ConfigurationService;
import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.config.implementation.PluginConfig;
import com.eternalcode.friends.database.DataSourceBuilder;
import com.eternalcode.friends.database.FriendDatabaseService;
import com.eternalcode.friends.database.IgnoredPlayerDatabaseService;
import com.eternalcode.friends.database.InviteDatabaseService;
import com.eternalcode.friends.friend.FriendManager;
import com.eternalcode.friends.gui.MainGui;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.listener.AnnounceJoinListener;
import com.eternalcode.friends.listener.AsyncPlayerChatListener;
import com.eternalcode.friends.listener.EntityDamageByEntityListener;
import com.eternalcode.friends.listener.NametagJoinQuitListener;
import com.eternalcode.friends.packet.NameTagService;
import com.eternalcode.friends.util.legacy.LegacyColorProcessor;
import com.zaxxer.hikari.HikariDataSource;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.adventure.paper.LitePaperAdventureFactory;
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

import java.io.File;
import java.util.stream.Stream;

public class EternalFriends extends JavaPlugin {

    private NotificationAnnouncer announcer;
    private ConfigurationService configurationService;
    private MainGui mainGui;
    private InviteManager inviteManager;
    private NameTagService nameTagService;
    private AudienceProvider audienceProvider;
    private MiniMessage miniMessage;
    private ProtocolManager protocolManager;
    private LiteCommands<CommandSender> liteCommands;
    private FriendManager friendManager;
    private FriendDatabaseService friendDatabaseService;
    private IgnoredPlayerDatabaseService ignoredPlayerDatabaseService;
    private InviteDatabaseService inviteDatabaseService;

    private HikariDataSource databaseDataSource;

    @Override
    public void onLoad() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        Server server = this.getServer();

        this.audienceProvider = BukkitAudiences.create(this);
        this.miniMessage = MiniMessage.builder().postProcessor(new LegacyColorProcessor()).build();

        this.announcer = new NotificationAnnouncer(this.audienceProvider, this.miniMessage);

        this.configurationService = new ConfigurationService();

        PluginConfig config = configurationService.create(PluginConfig.class, new File(this.getDataFolder(), "config.yml"));
        MessagesConfig messages = configurationService.create(MessagesConfig.class, new File(this.getDataFolder(), "messages.yml"));
        GuiConfig guiConfig = configurationService.create(GuiConfig.class, new File(this.getDataFolder(), "gui.yml"));

        this.databaseDataSource = new DataSourceBuilder().buildHikariDataSource(config.database, this.getDataFolder());

        this.friendDatabaseService = new FriendDatabaseService(this.databaseDataSource);
        this.ignoredPlayerDatabaseService = new IgnoredPlayerDatabaseService(this.databaseDataSource);
        this.inviteDatabaseService = new InviteDatabaseService(this.databaseDataSource);

        this.nameTagService = new NameTagService(this.protocolManager, config);

        this.inviteManager = new InviteManager(config, this.inviteDatabaseService);

        this.friendManager = new FriendManager(this.friendDatabaseService, this.ignoredPlayerDatabaseService, this.inviteManager);

        this.mainGui = new MainGui(this.miniMessage, guiConfig, this, this.announcer, messages, this.inviteManager, this.friendManager, this.nameTagService);

        Stream.of(
            new NametagJoinQuitListener(this.protocolManager, this.nameTagService, this.friendManager),
            new AnnounceJoinListener(config, messages, this.friendManager, this.announcer),
            new EntityDamageByEntityListener(this.friendManager),
            new AsyncPlayerChatListener(this.announcer, messages, this.friendManager)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));

        new Metrics(this, 16297);

        this.liteCommands = LitePaperAdventureFactory.builder(server, "eternalfriends")
            .argument(Player.class, new BukkitPlayerArgument<>(server, messages.argument.playerNotFound))

            .invalidUsageHandler(new InvalidUsage(messages, this.announcer))
            .permissionHandler(new PermissionMessage(messages, this.announcer))

            .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>(messages.argument.playerOnly))

            .commandInstance(
                new FriendCommand(this.mainGui),
                new FriendAcceptCommand(this.announcer, this.inviteManager, messages, this.nameTagService, this.friendManager),
                new FriendDenyCommand(this.announcer, this.inviteManager, messages),
                new FriendInviteCommand(this.announcer, this.inviteManager, messages, this.friendManager),
                new FriendListCommand(this.announcer, messages, this.getServer(), this.friendManager),
                new FriendKickCommand(this.announcer, messages, this.nameTagService, this.friendManager),
                new FriendIgnoreCommand(this.announcer, messages, this.friendManager),
                new FriendHelpCommand(this.announcer, messages),
                new FriendReloadCommand(this.announcer, messages, this.configurationService)
            )

            .commandEditor("friends", new CommandConfigurator(config))

            .register();
    }

    @Override
    public void onDisable() {
        if (this.liteCommands != null) {
            this.liteCommands.getPlatform().unregisterAll();
        }
    }
}
