package me.swerve.hub.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJumpListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE && player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR && !player.isInsideVehicle())
            player.setAllowFlight(true);
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();

        if (p.getGameMode() == GameMode.CREATIVE) return;

        e.setCancelled(true);
        p.setAllowFlight(false);
        p.setFlying(false);

        p.setVelocity(p.getLocation().getDirection().multiply(1.5D).setY(0.75D));
        p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
    }
}
