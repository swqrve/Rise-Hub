package me.swerve.hub.listener;

import me.swerve.hub.hotbar.HubHotbar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");

        Player p = e.getPlayer();

        p.teleport(new Location(Bukkit.getWorld("Lobby"), 12, 54, -0.5, 90, -1.3f));
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
