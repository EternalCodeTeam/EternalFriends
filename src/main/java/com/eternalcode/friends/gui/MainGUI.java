package com.eternalcode.friends.gui;

import com.eternalcode.friends.EternalFriends;
import com.eternalcode.friends.util.ChatUtils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class MainGUI {

    private final String mainInvTitle = "Friends",
            friendListItemName = "Friend List";


    private Gui mainInv;

    public MainGUI(){
        GuiItem friendList = ItemBuilder.from(Material.BOOK)
                .name(Component.text(ChatUtils.colored(friendListItemName)))
                .asGuiItem();
        mainInv = Gui.gui()
                .title(Component.text(ChatUtils.colored(mainInvTitle)))
                .rows(3)
                .disableItemTake()
                .create();
        mainInv.setItem(2, 3, friendList);
    }

    public Gui getMainInv() {
        return mainInv;
    }
}
