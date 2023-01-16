package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.config.ReloadableConfig;
import net.dzikoysk.cdn.entity.Contextual;
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

    public MenuItems menuItems = new MenuItems();
    public Guis guis = new Guis();

    
    @Contextual
    public static class MenuItems {

        public FriendHead friendListHead = new FriendHead()
                .setLore(List.of("&cKliknij LPM", "&caby usunąć znajomego"));
        public FriendHead inviteListfriendHead = new FriendHead()
                .setLore(List.of("&aKliknij LPM, aby zaakceptowac zaproszenie", "&cKliknij PPM, aby odrzucic zaproszenie"));

        public ConfigItem nextPageItem = new ConfigItem()
                .setName("&aNastępna strona")
                .setType(Material.PAPER)
                .setLore(List.of(
                        "&7Kliknij aby przejść do następnej strony"
                ));

        public ConfigItem previousPageItem = new ConfigItem()
                .setName("&cPoprzednia strona")
                .setType(Material.PAPER)
                .setLore(List.of(
                        "&7Kliknij aby przejść do poprzedniej strony"
                ));

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

        public ConfigItem receivedInvitesItem = new ConfigItem()
                .setName("&aOtrzymane zaproszenia")
                .setType(Material.BOOK)
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
        public ConfigItem backToMainMenuItem = new ConfigItem()
                .setName("&7Powrot do menu glownego")
                .setType(Material.ARROW)
                .setLore(List.of());
    }

    @Contextual
    public static class Guis {
        public String mainGuiTitle = "&bFriends";
        public String confirmGuiTitle = "&cJestes pewien?";
        public String receivedInvitesGuiTitle = "&bOtrzymane zaproszenia";
    }

    @Contextual
    public static class FriendHead {
        public String name = "&f%friend_name%";
        public List<String> lore = List.of("&fFirst line of lore", "&9Second line of lore");

        public FriendHead setLore(List<String> lore) {
            this.lore = lore;
            return this;
        }
    }
}
