package com.eternalcode.friends.gui;

import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;

public class MainGUI {

    private final String mainInvTitle = "Friends";

    private Gui mainInv;

    public MainGUI(){
        mainInv = Gui.gui()
                .title(Component.text(mainInvTitle))
                .rows(3)
                .disableItemTake()
                .create();
    }

    public Gui getMainInv() {
        return mainInv;
    }
}
