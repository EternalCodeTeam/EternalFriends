package com.eternalcode.friends.gui;

import com.eternalcode.friends.config.implementation.GuiConfig;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;


public class MainGUI {

    private final MiniMessage miniMessage;
    private final GuiConfig guiConfig;

    private Material type;

    private Gui gui;


    public MainGUI(MiniMessage miniMessage, GuiConfig guiConfig) {
        this.miniMessage = miniMessage;
        this.guiConfig = guiConfig;
        initializeGui();
    }

    private void initializeGui() {
        GuiConfig.MainGui mainGui = guiConfig.mainGui;
        GuiConfig.ConfigItem friendListItem = guiConfig.friendListItem;
        GuiConfig.ConfigItem receivedAndSentInvitesItem = guiConfig.receivedAndSentInvitesItem;
        GuiConfig.ConfigItem sendInvitesItem = guiConfig.sendInvitesItem;
        GuiConfig.ConfigItem settingItem = guiConfig.settingItem;
        this.gui = Gui.gui()
                .title(this.miniMessage.deserialize(mainGui.title))
                .rows(mainGui.rows)
                .disableItemTake()
                .create();

        gui.setItem(friendListItem.slot, friendListItem.toGuiItem());
        gui.setItem(receivedAndSentInvitesItem.slot, receivedAndSentInvitesItem.toGuiItem());
        gui.setItem(sendInvitesItem.slot, sendInvitesItem.toGuiItem());
        gui.setItem(settingItem.slot, settingItem.toGuiItem());
    }

    public void openInventory(Player player) {
        gui.open(player);
    }
}