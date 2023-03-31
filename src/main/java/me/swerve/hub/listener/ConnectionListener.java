package me.swerve.hub.listener;

import me.swerve.hub.hotbar.HubHotbar;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");

        Player p = e.getPlayer();

        p.getInventory().clear();
        p.setGameMode(GameMode.SURVIVAL);
        p.setExp(0);
        p.teleport(new Location(Bukkit.getWorld("Lobby"), 0.5, 50, 0.5, -180, -1.3f));
        p.playSound(p.getLocation(), Sound.valueOf("LEVEL_UP"), 1f, 1);

        List<String> message = Arrays.asList(
                "&7&m----------------------------------",
                "&fWelcome to the &6&lRise Network",
                " ",
                "&fStore: &6Coming soon...",
                "&fDiscord: &9https://discord.gg/2E54g9pg7c",
                "&7&m----------------------------------");

        List<String> translated = new ArrayList<>();
        message.forEach(m -> translated.add(ChatColor.translateAlternateColorCodes('&', m)));

        for (String s : translated) p.sendMessage(s);

        HubHotbar.getInstance().giveItems(p);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        e.setQuitMessage("");
    }
}
