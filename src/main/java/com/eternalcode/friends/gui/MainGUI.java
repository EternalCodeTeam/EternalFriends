package com.eternalcode.friends.gui;

import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.util.ColorUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class MainGUI {

    private final MessagesConfig messages;

    private final String mainInvTitle = "Friends", friendListItemName = "Friend List";
    private final Gui mainInventory;

    public MainGUI(MessagesConfig messages) {
        this.messages = messages;

        GuiItem friendList = ItemBuilder.from(Material.BOOK)
                .name(Component.text(ColorUtil.colored("")))
                .asGuiItem();
        mainInventory = Gui.gui()
                .title(Component.text(ColorUtil.colored(mainInvTitle)))
                .rows(3)
                .disableItemTake()
                .create();
        mainInventory.setItem(2, 3, friendList);
    }

    public Gui getMainInventory() {
        return mainInventory;
    }
}
