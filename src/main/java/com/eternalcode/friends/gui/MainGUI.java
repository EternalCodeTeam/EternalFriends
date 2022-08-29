package com.eternalcode.friends.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainGUI {

    private final MiniMessage miniMessage;

    private final String mainInventoryTitle = "Friends", friendListItemName = "Friend List";

    public MainGUI(MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
    }

    public void openInventory(Player player) {

        GuiItem friendList = ItemBuilder.from(Material.BOOK)
                .name(this.miniMessage.deserialize("&a" + this.friendListItemName))
                .asGuiItem();

        Gui gui = Gui.gui()
                .title(this.miniMessage.deserialize(mainInventoryTitle))
                .rows(3)
                .disableItemTake()
                .create();

        gui.setItem(2, 3, friendList);

        gui.open(player);
    }
}