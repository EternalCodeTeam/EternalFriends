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

        GuiItem friendListItem = new GuiItemBuilder(guiConfig.friendListItem, miniMessage).get();

        GuiItem receivedAndSentInvitesItem = new GuiItemBuilder(guiConfig.receivedAndSentInvitesItem, miniMessage).get();

        GuiItem sendInvitesItem = new GuiItemBuilder(guiConfig.sendInvitesItem, miniMessage).get();

        GuiItem settingItemItem = new GuiItemBuilder(guiConfig.settingItem, miniMessage).get();

        this.gui = Gui.gui()
                .title(this.miniMessage.deserialize(guiConfig.mainGui.title))
                .rows(guiConfig.mainGui.rows)
                .disableItemTake()
                .create();

        gui.setItem(guiConfig.mainGui.friendListItemSlot, friendListItem);
        gui.setItem(guiConfig.mainGui.receivedAndSentInvitesItemSlot, receivedAndSentInvitesItem);
        gui.setItem(guiConfig.mainGui.sendInviteItemSlot, sendInvitesItem);
        gui.setItem(guiConfig.mainGui.settingsItemSlot, settingItemItem);
    }

    public void openInventory(Player player) {
        gui.open(player);
    }
}