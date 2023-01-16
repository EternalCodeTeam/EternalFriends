package com.eternalcode.friends.gui;

import com.eternalcode.friends.config.implementation.GuiConfig;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class ConfirmGui {

    private GuiConfig guiConfig;

    private final MiniMessage miniMessage;

    private final static int CONFIRM_ITEM_SLOT = 11;
    private final static int DENY_ITEM_SLOT = 15;

    public ConfirmGui(GuiConfig guiConfig, MiniMessage miniMessage) {
        this.guiConfig = guiConfig;
        this.miniMessage = miniMessage;
    }

    public void openInventory(Player player, Runnable onConfirm, Runnable onDeny) {
        GuiItem confirmItem = this.guiConfig.menuItems.confirmItem.toGuiItem(this.miniMessage);
        confirmItem.setAction(event -> onConfirm.run());

        GuiItem denyItem = this.guiConfig.menuItems.denyItem.toGuiItem(this.miniMessage);
        denyItem.setAction(event -> onDeny.run());

        Gui gui = Gui.gui()
                .title(this.miniMessage.deserialize(this.guiConfig.guis.confirmGuiTitle))
                .rows(3)
                .disableItemTake()
                .create();

        gui.getFiller().fill(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.text(" ")).flags(ItemFlag.HIDE_ATTRIBUTES).asGuiItem());

        gui.setItem(CONFIRM_ITEM_SLOT, confirmItem);
        gui.setItem(DENY_ITEM_SLOT, denyItem);

        gui.open(player);
    }


}
