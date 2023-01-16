package com.eternalcode.friends;

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
import com.eternalcode.friends.config.ConfigManager;
import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.config.implementation.PluginConfig;
import com.eternalcode.friends.gui.MainGui;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.listener.AsyncPlayerChatListener;
import com.eternalcode.friends.listener.EntityDamageByEntityListener;
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

        this.inviteManager = new InviteManager();

        this.profileManager = new ProfileManager(new ProfileRepositoryImpl());

        this.mainGui = new MainGui(this.miniMessage, this.guiConfig, this, this.profileManager, this.announcer, this.messages, this.inviteManager);

        Stream.of(
                new ProfileJoinListener(this.profileManager),
                new EntityDamageByEntityListener(this.profileManager),
                new AsyncPlayerChatListener(this.profileManager, this.announcer, this.messages)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));

        Metrics metrics = new Metrics(this, 16297);

        this.liteCommands = LiteBukkitFactory.builder(server, "eternalfriends")
                .argument(Player.class, new BukkitPlayerArgument<>(server, this.messages.argument.playerNotFound))

                .invalidUsageHandler(new InvalidUsage(this.messages, this.announcer))
                .permissionHandler(new PermissionMessage(this.messages, this.announcer))

                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>(this.messages.argument.playerOnly))

                .commandInstance(new FriendCommand(this.mainGui))
                .commandInstance(new FriendAcceptCommand(this.profileManager, this.announcer, this.inviteManager, this.messages))
                .commandInstance(new FriendDenyCommand(this.profileManager, this.announcer, this.inviteManager, this.messages))
                .commandInstance(new FriendInviteCommand(this.profileManager, this.announcer, this.inviteManager, this.messages))
                .commandInstance(new FriendListCommand(this.profileManager, this.announcer, this.messages, this.getServer()))
                .commandInstance(new FriendKickCommand(this.profileManager, this.announcer, this.messages))
                .commandInstance(new FriendIgnoreCommand(this.profileManager, this.announcer, this.messages))
                .commandInstance(new FriendHelpCommand(this.announcer, this.messages))
                .commandInstance(new FriendReloadCommand(this.announcer, this.messages, this.configManager))

                .commandEditor("friends", new CommandConfigurator(this.config))

                .register();

    }

    @Override
    public void onDisable() {
        this.liteCommands.getPlatform().unregisterAll();
    }
}
