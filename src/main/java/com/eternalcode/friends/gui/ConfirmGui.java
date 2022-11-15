package com.eternalcode.friends.gui;

import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.util.legacy.Legacy;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.function.Consumer;

public class ConfirmGui {

    private GuiConfig guiConfig;

    private final static int CONFIRM_ITEM_SLOT = 15;
    private final static int DENY_ITEM_SLOT = 15;

    public ConfirmGui(GuiConfig guiConfig) {
        this.guiConfig = guiConfig;
    }



    public void openInventory(Player player, Consumer<Player> returnWithConfirm, Consumer<Player> returnWithDeny) {
        GuiItem confirmItem = guiConfig.confirmGui.confirmItem.toGuiItem();
        confirmItem.setAction(event -> returnWithConfirm.accept(player));

        GuiItem denyItem = guiConfig.confirmGui.denyItem.toGuiItem();
        denyItem.setAction(event -> returnWithDeny.accept(player));

        Gui gui = Gui.gui()
                .title(Legacy.AMPERSAND_SERIALIZER.deserialize(guiConfig.confirmGui.title))
                .rows(3)
                .disableItemTake()
                .create();

        gui.getFiller().fill(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.text(" ")).flags(ItemFlag.HIDE_ATTRIBUTES).asGuiItem());
        gui.setItem(CONFIRM_ITEM_SLOT, confirmItem);
        gui.setItem(DENY_ITEM_SLOT, denyItem);
        gui.open(player);
    }


}
