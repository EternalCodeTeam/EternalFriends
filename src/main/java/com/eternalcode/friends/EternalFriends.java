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
import com.eternalcode.friends.config.ConfigManager;
import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.config.implementation.PluginConfig;
import com.eternalcode.friends.friend.FriendManager;
import com.eternalcode.friends.gui.MainGui;
import com.eternalcode.friends.invite.InviteManager;
import com.eternalcode.friends.listener.AsyncPlayerChatListener;
import com.eternalcode.friends.listener.EntityDamageByEntityListener;
import com.eternalcode.friends.packet.NameTagService;
import com.eternalcode.friends.listener.JoinQuitListener;
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
    private NameTagService nameTagService;
    private AudienceProvider audienceProvider;
    private MiniMessage miniMessage;
    private ProtocolManager protocolManager;
    private LiteCommands<CommandSender> liteCommands;
    private FriendManager friendManager;

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

        this.configManager = new ConfigManager(this.getDataFolder());

        this.config = new PluginConfig();
        this.messages = new MessagesConfig();
        this.guiConfig = new GuiConfig();

        this.configManager.load(this.config);
        this.configManager.load(this.messages);
        this.configManager.load(this.guiConfig);

        this.nameTagService = new NameTagService(this.protocolManager, this.config);

        this.inviteManager = new InviteManager();

        this.friendManager = new FriendManager();

        this.mainGui = new MainGui(this.miniMessage, this.guiConfig, this, this.announcer, this.messages, this.inviteManager, this.friendManager, this.nameTagService);

        Stream.of(
                new JoinQuitListener(this.protocolManager, this.nameTagService, this.friendManager),
                new EntityDamageByEntityListener(this.friendManager),
                new AsyncPlayerChatListener(this.announcer, this.messages, this.friendManager)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));

        Metrics metrics = new Metrics(this, 16297);

        this.liteCommands = LiteBukkitFactory.builder(server, "eternalfriends")
                .argument(Player.class, new BukkitPlayerArgument<>(server, this.messages.argument.playerNotFound))

                .invalidUsageHandler(new InvalidUsage(this.messages, this.announcer))
                .permissionHandler(new PermissionMessage(this.messages, this.announcer))

                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>(this.messages.argument.playerOnly))

                .commandInstance(new FriendCommand(this.mainGui))
                .commandInstance(new FriendAcceptCommand(this.announcer, this.inviteManager, this.messages, this.nameTagService, this.friendManager))
                .commandInstance(new FriendDenyCommand(this.announcer, this.inviteManager, this.messages))
                .commandInstance(new FriendInviteCommand(this.announcer, this.inviteManager, this.messages, this.friendManager))
                .commandInstance(new FriendListCommand(this.announcer, this.messages, this.getServer(), this.friendManager))
                .commandInstance(new FriendKickCommand(this.announcer, this.messages, this.nameTagService, this.friendManager))
                .commandInstance(new FriendIgnoreCommand(this.announcer, this.messages, this.friendManager))
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
