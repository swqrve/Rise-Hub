package me.swerve.hub.hotbar;

import lombok.Getter;
import me.swerve.hub.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
public class HubHotbar {
    @Getter private static HubHotbar instance;

    @Getter private final List<UUID> toggledVisibility = new ArrayList<>();
    @Getter private final HashMap<UUID, Long> lastToggledVisibility = new HashMap<>();

    @Getter
    private final ItemCreator serverSelector = new ItemCreator(Material.COMPASS, 1).setName("&6Server Selector").addLore(Collections.singletonList("&6Select a server to join!"));
    @Getter
    private final ItemCreator enderButt = new ItemCreator(Material.ENDER_PEARL, 1).setName("&5Ender Butt").addLore(Collections.singletonList("&5Fly through the air on an enderpearl!"));
    @Getter
    private final ItemCreator toggleVisibility = new ItemCreator(Material.INK_SACK, 1).setData(10).setName("&aToggle Visibility").addLore(Collections.singletonList("&7Right click to toggle visibility"));

    public HubHotbar() {
        instance = this;
    }

    public void giveItems(Player p) {
        p.getInventory().clear();

        p.getInventory().setItem(2, serverSelector.getItem());
        p.getInventory().setItem(4, enderButt.getItem());
        p.getInventory().setItem(6, toggleVisibility.getItem());
    }
}
