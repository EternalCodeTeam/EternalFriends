package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.gui.MainGui;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

@Route(name = "friends")
public class FriendCommand {

    private final MainGui mainGui;

    public FriendCommand(MainGui mainGui) {
        this.mainGui = mainGui;
    }

    @Execute(required = 0)
    @Permission("eternalfriends.access.gui")
    void main(Player player) {
        this.mainGui.openMainGui(player);
    }


}
