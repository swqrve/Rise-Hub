package me.swerve.hub.listener;

import me.swerve.hub.RiseHub;
import me.swerve.hub.hotbar.HubHotbar;
import me.swerve.hub.menu.board.ServerSelector;
import me.swerve.hub.util.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class InteractListener implements Listener {
    DecimalFormat df = new DecimalFormat("#.##",new DecimalFormatSymbols(Locale.US));

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) {
            e.setCancelled(true);
            return;
        }

        if (e.getItem() == null) return;

        Player p = e.getPlayer();
        HubHotbar manager = HubHotbar.getInstance();

        if (e.getItem().getType() == Material.INK_SACK) {
            byte data = p.getItemInHand().getData().getData();

            if (manager.getLastToggledVisibility().get(p.getUniqueId()) != null && System.currentTimeMillis() - ((manager.getLastToggledVisibility().get(p.getUniqueId()))) < 3000L) {
                long countDownTime = 3000L - ((System.currentTimeMillis() - manager.getLastToggledVisibility().get(p.getUniqueId())));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have to wait another " + df.format(countDownTime / 1000L) + " seconds before you can use this again!"));
                return;
            }

            manager.getLastToggledVisibility().put(p.getUniqueId(), System.currentTimeMillis());

            if (data == 10) {
                Bukkit.getOnlinePlayers().forEach(p::hidePlayer);
                if (!manager.getToggledVisibility().contains(p.getUniqueId())) manager.getToggledVisibility().add(p.getUniqueId());

                ItemCreator clone = new ItemCreator(manager.getToggleVisibility());
                clone.setData(8);
                clone.setName(ChatColor.translateAlternateColorCodes('&', "&c" + ChatColor.stripColor(clone.getMeta().getDisplayName())));

                p.setItemInHand(clone.getItem());
                p.updateInventory();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can no longer see other players."));
                return;
            }

            Bukkit.getOnlinePlayers().forEach(p::showPlayer);
            manager.getToggledVisibility().remove(p.getUniqueId());
            p.setItemInHand(manager.getToggleVisibility().getItem());
            p.updateInventory();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou can now see other players."));

            return;
        }

        if (e.getItem().getType() == Material.COMPASS) {
            Bukkit.getPluginManager().registerEvents(new ServerSelector(e.getPlayer()), RiseHub.getInstance());
            return;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getGameMode() != GameMode.CREATIVE) e.setCancelled(true);
    }
}
