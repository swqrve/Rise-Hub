package me.swerve.hub.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class SpawnListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (e.getEntity().getType() == EntityType.DROPPED_ITEM) return;
        e.setCancelled(true);
    }
}
