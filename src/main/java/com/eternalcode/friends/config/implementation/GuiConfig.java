package com.eternalcode.friends.config.implementation;

import com.eternalcode.friends.config.ReloadableConfig;
import com.eternalcode.friends.util.legacy.Legacy;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuiConfig implements ReloadableConfig {

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "gui.yml");
    }

    public MainGui mainGui = new MainGui();

    public Gui friendListGui = new Gui()
            .setTitle("&8Lista znajomych");

    public ConfirmGui confirmGui = new ConfirmGui();

    public FriendHead friendHead = new FriendHead();
    public ConfigItem friendListItem = new ConfigItem()
            .setSlot(11)
            .setName("&aTwoja lista znajomych")
            .setType(Material.BOOK)
            .setLore(Arrays.asList(
                    "&7Lista znajomych",
                    "&7Kliknij aby otworzyć"
            ));
    public ConfigItem receivedAndSentInvitesItem = new ConfigItem()
            .setSlot(13)
            .setName("&aOtrzymane i wysłane zaproszenia")
            .setType(Material.WRITABLE_BOOK)
            .setLore(Arrays.asList(
                    "&7Otrzymane i wysłane zaproszenia",
                    "&7Kliknij aby otworzyć"
            ));
    public ConfigItem sendInvitesItem = new ConfigItem()
            .setSlot(15)
            .setName("&aWysyłanie zaproszeń")
            .setType(Material.PAPER)
            .setLore(Arrays.asList(
                    "&7Wysyłanie zaproszeń",
                    "&7Kliknij aby otworzyć"
            ));
    public ConfigItem settingItem = new ConfigItem()
            .setSlot(18)
            .setName("&aUstawienia")
            .setType(Material.REPEATER)
            .setLore(Arrays.asList(
                    "&7Ustawienia",
                    "&7Kliknij aby otworzyć"
            ));
    public ConfigItem backButton = new ConfigItem()
            .setSlot(26)
            .setName("&cWróć")
            .setType(Material.BARRIER)
            .setLore(Arrays.asList(
                    "&7Wróć do menu głównego",
                    "&7Kliknij aby otworzyć"
            ));

    @Contextual
    public static class MainGui {
        @Description("# Rows of inventory (up to 6)")
        public int rows = 3;

        @Description("# Title of inventory")
        public String title = "&bFriends";
    }

    @Contextual
    public static class Gui {
        @Description("# Title of inventory")
        public String title = "&bGui Title";

        public  Gui setTitle(String title) {
            this.title = title;
            return this;
        }
    }

    @Contextual
    public static class ConfigItem {

        public int slot = 0;
        @Description("# Material from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html")
        public Material type = Material.STONE;

        public String name = "&fItem name";

        @Description("# Description of item")
        public List<String> lore = Arrays.asList("&fFirst line of lore", "&9Second line of lore");

        @Description("# Item should be enchanted or not")
        public boolean enchanted = false;

        public GuiItem toGuiItem() {
            ItemBuilder builder = ItemBuilder.from(this.type)
                    .name(Legacy.component(this.name))
                    .lore(this.lore.stream().map(Legacy::component).collect(Collectors.toList()))
                    .flags(ItemFlag.HIDE_ATTRIBUTES)
                    .flags(ItemFlag.HIDE_ENCHANTS);
            if (this.enchanted) {
                builder.enchant(Enchantment.LOOT_BONUS_BLOCKS);
            }
            return builder.asGuiItem();
        }

        public ConfigItem setSlot(int slot) {
            this.slot = slot;
            return this;
        }

        public ConfigItem setType(Material type) {
            this.type = type;
            return this;
        }

        public ConfigItem setName(String name) {
            this.name = name;
            return this;
        }

        public ConfigItem setLore(List<String> lore) {
            this.lore = lore;
            return this;
        }

        public ConfigItem setEnchanted(boolean enchanted) {
            this.enchanted = enchanted;
            return this;
        }
    }

    @Contextual
    public static class FriendHead {
        public String name = "&f%friend_name%";
        public List<String> lore = Arrays.asList("&cKliknij LPM", "&caby usunąć znajomego");
    }

    @Contextual
    public static class ConfirmGui {
        public String title = "&cJESTES PEWIEN?";

        public ConfigItem confirmItem = new ConfigItem()
                .setSlot(11)
                .setName("&aTak")
                .setType(Material.LIME_STAINED_GLASS_PANE)
                .setLore(Arrays.asList(
                        "&7Kliknij aby potwierdzić"
                ));

        public ConfigItem denyItem = new ConfigItem()
                .setSlot(15)
                .setName("&cNie")
                .setType(Material.RED_STAINED_GLASS_PANE)
                .setLore(Arrays.asList(
                        "&7Kliknij aby anulować"
                ));
    }
}
