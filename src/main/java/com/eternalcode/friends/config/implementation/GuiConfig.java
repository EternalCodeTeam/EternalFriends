package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.config.ReloadableConfig;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import org.bukkit.Material;

import java.io.File;
import java.util.List;

public class GuiConfig implements ReloadableConfig {

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "gui.yml");
    }

    public MainGui mainGui = new MainGui();

    public ConfirmGui confirmGui = new ConfirmGui();

    public FriendHead friendHead = new FriendHead();
    public ConfigItem receivedInvitesItem = new ConfigItem()
            .setName("&aOtrzymane zaproszenia")
            .setType(Material.WRITABLE_BOOK)
            .setLore(List.of(
                    "&7Otrzymane zaproszenia",
                    "&7Kliknij aby otworzyć"
            ));
    public ConfigItem sendInvitesItem = new ConfigItem()
            .setName("&aWysyłanie zaproszeń")
            .setType(Material.WRITABLE_BOOK)
            .setLore(List.of(
                    "&7Wysyłanie zaproszeń",
                    "&7Kliknij aby otworzyć"
            ));
    public ConfigItem settingItem = new ConfigItem()
            .setName("&aUstawienia")
            .setType(Material.REPEATER)
            .setLore(List.of(
                    "&7Ustawienia",
                    "&7Kliknij aby otworzyć"
            ));

    @Contextual
    public static class MainGui {
        @Description("# Rows of inventory (up to 6)")
        public int rows = 3;

        @Description("# Title of inventory")
        public String title = "&bFriends";

        public String nextPageItemName = "&aNastępna strona";

        public List<String> nextPageItemLore = List.of(
                "&7Kliknij aby przejść do następnej strony"
        );

        public String previousPageItemName = "&cPoprzednia strona";

        public List<String> previousPageItemLore = List.of(
                "&7Kliknij aby przejść do poprzedniej strony"
        );
    }

    @Contextual
    public static class FriendHead {
        public String name = "&f%friend_name%";
        public List<String> lore = List.of("&cKliknij LPM", "&caby usunąć znajomego");
    }

    @Contextual
    public static class ConfirmGui {
        public String title = "&cJestes pewien?";

        public ConfigItem confirmItem = new ConfigItem()
                .setName("&aTak")
                .setType(Material.LIME_STAINED_GLASS_PANE)
                .setLore(List.of(
                        "&7Kliknij aby potwierdzić"
                ));

        public ConfigItem denyItem = new ConfigItem()
                .setName("&cNie")
                .setType(Material.RED_STAINED_GLASS_PANE)
                .setLore(List.of(
                        "&7Kliknij aby anulować"
                ));
    }
}
