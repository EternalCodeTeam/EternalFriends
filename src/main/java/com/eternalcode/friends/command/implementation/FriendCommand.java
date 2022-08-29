package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.gui.MainGUI;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import org.bukkit.entity.Player;

@Section(route = "friend", aliases = {"friends", "przyjaciele", "znajomi"})
public class FriendCommand {

    private final MainGUI mainGui;

    public FriendCommand(MainGUI mainGui) {
        this.mainGui = mainGui;
    }

    @Execute
    void main(Player player) {
        this.mainGui.openInventory(player);
    }
}
