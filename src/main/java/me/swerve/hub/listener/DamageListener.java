package me.swerve.hub.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        e.setCancelled(true);

        if (!(e.getEntity() instanceof Player)) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
            e.getEntity().teleport(new Location(Bukkit.getWorld("Lobby"), 12, 54, -0.5, 90, -1.3f));
            ((Player) e.getEntity()).playSound(e.getEntity().getLocation(), Sound.valueOf("LEVEL_UP"), 1f, 1);
        }
    }
}
