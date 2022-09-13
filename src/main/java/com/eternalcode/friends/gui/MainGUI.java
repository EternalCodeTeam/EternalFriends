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
        ItemBuilder friendListBuilder = ItemBuilder.from(Material.getMaterial(guiConfig.friendListItem.type) != null ? Material.getMaterial(guiConfig.friendListItem.type) : Material.STONE)
                .name(this.miniMessage.deserialize(guiConfig.friendListItem.name))
                .lore(fromStringListToComponentList(guiConfig.friendListItem.lore))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_ENCHANTS);
        if (guiConfig.friendListItem.enchanted) {
            friendListBuilder.enchant(Enchantment.LOOT_BONUS_BLOCKS);
        }
        GuiItem friendListItem = friendListBuilder.asGuiItem();

        ItemBuilder receivedAndSentInvitesBuilder = ItemBuilder.from(Material.getMaterial(guiConfig.receivedAndSentInvitesItem.type) != null ? Material.getMaterial(guiConfig.receivedAndSentInvitesItem.type) : Material.STONE)
                .name(this.miniMessage.deserialize(guiConfig.receivedAndSentInvitesItem.name))
                .lore(fromStringListToComponentList(guiConfig.receivedAndSentInvitesItem.lore))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_ENCHANTS);
        if (guiConfig.receivedAndSentInvitesItem.enchanted) {
            receivedAndSentInvitesBuilder.enchant(Enchantment.LOOT_BONUS_BLOCKS);
        }
        GuiItem receivedAndSentInvitesItem = receivedAndSentInvitesBuilder.asGuiItem();

        ItemBuilder sendInvitesBuilder = ItemBuilder.from(Material.getMaterial(guiConfig.sendInvitesItem.type) != null ? Material.getMaterial(guiConfig.sendInvitesItem.type) : Material.STONE)
                .name(this.miniMessage.deserialize(guiConfig.sendInvitesItem.name))
                .lore(fromStringListToComponentList(guiConfig.sendInvitesItem.lore))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_ENCHANTS);
        if (guiConfig.sendInvitesItem.enchanted) {
            sendInvitesBuilder.enchant(Enchantment.LOOT_BONUS_BLOCKS);
        }
        GuiItem sendInvitesItem = sendInvitesBuilder.asGuiItem();

        ItemBuilder settingItemBuilder = ItemBuilder.from(Material.getMaterial(guiConfig.settingItem.type) != null ? Material.getMaterial(guiConfig.settingItem.type) : Material.STONE)
                .name(this.miniMessage.deserialize(guiConfig.settingItem.name))
                .lore(fromStringListToComponentList(guiConfig.settingItem.lore))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_ENCHANTS);
        if (guiConfig.settingItem.enchanted) {
            settingItemBuilder.enchant(Enchantment.LOOT_BONUS_BLOCKS);
        }
        GuiItem settingItemItem = settingItemBuilder.asGuiItem();

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

    private List<Component> fromStringListToComponentList(List<String> lines) {
        List<Component> lore = new ArrayList<>();
        for (String line : lines) {
            lore.add(miniMessage.deserialize(line));
        }
        return lore;
    }
}