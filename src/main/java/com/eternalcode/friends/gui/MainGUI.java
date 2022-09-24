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
        List<GuiConfig.ConfigItem> items = new ArrayList<>();
        items.add(guiConfig.friendListItem);
        items.add(guiConfig.receivedAndSentInvitesItem);
        items.add(guiConfig.sendInvitesItem);
        items.add(guiConfig.settingItem);
        this.gui = Gui.gui()
                .title(this.miniMessage.deserialize(mainGui.title))
                .rows(mainGui.rows)
                .disableItemTake()
                .create();

        for(GuiConfig.ConfigItem item : items) {
            this.gui.setItem(item.slot, item.toGuiItem());
        }
    }

    public void openInventory(Player player) {
        gui.open(player);
    }
}