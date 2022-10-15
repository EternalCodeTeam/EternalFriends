package com.eternalcode.friends.gui;

import com.eternalcode.friends.config.implementation.GuiConfig;
import com.eternalcode.friends.util.legacy.Legacy;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ConfirmGUI {

    private GuiConfig guiConfig;

    public ConfirmGUI(GuiConfig guiConfig) {
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
        gui.setItem(guiConfig.confirmGui.confirmItem.slot, confirmItem);
        gui.setItem(guiConfig.confirmGui.denyItem.slot, denyItem);
        gui.open(player);
    }


}
