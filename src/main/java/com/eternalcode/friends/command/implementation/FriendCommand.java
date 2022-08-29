package com.eternalcode.friends.command.implementation;

import com.eternalcode.friends.gui.MainGUI;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import org.bukkit.entity.Player;

@Section(route = "friend", aliases = {"friends", "przyjaciele", "znajomi"})
public class FriendCommand {


    @Execute
    public void cmd(Player sender){
        sender.openInventory(new MainGUI().getMainInv().getInventory());
    }
}
