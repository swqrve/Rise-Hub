package me.swerve.hub.listener;

import me.swerve.hub.RiseHub;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EnderButtListener implements Listener {

    @EventHandler
    public void onThrowEnder(PlayerInteractEvent e) {
      Action action = e.getAction();
      if (!e.hasItem() || !e.getItem().getType().equals(Material.ENDER_PEARL) || (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK))) return;

      Player player = e.getPlayer();

      e.setCancelled(true);
      e.setUseItemInHand(Event.Result.DENY);
      e.setUseInteractedBlock(Event.Result.DENY);

      if (player.isInsideVehicle()) {
          player.getVehicle().remove();
          player.eject();
      }

      Item ep = player.getWorld().dropItem(player.getLocation().add(0.0D, 0.5D, 0.0D), new ItemStack(Material.ENDER_PEARL));
      ep.setPickupDelay(10000);

      ep.setVelocity(player.getLocation().getDirection().normalize().multiply(2.0F));
      ep.setPassenger(player);

      player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);

      deleteItemWhenNeeded(ep);
      player.updateInventory();
    }
    public void deleteItemWhenNeeded(Item item) {
      (new BukkitRunnable() {
          public void run() {
            if (item.isDead())
              cancel(); 
            if (item.getVelocity().getX() == 0.0D || item.getVelocity().getY() == 0.0D || item.getVelocity().getZ() == 0.0D) {
              Player p = (Player) item.getPassenger();
              item.remove();

              if (p != null)
                  p.teleport(p.getLocation().add(0.0D, 0.5D, 0.0D));

              cancel();
            } 
          }
        }).runTaskTimer(RiseHub.getInstance(), 2L, 1L);
    }
    
/*
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
      if (!(e.getEntity().getShooter() instanceof Player)) return;
      if (!(e.getEntity() instanceof org.bukkit.entity.EnderPearl)) return;

      Player p = (Player)e.getEntity().getShooter();
      Projectile proj = e.getEntity();

      if (proj.getType() != EntityType.ENDER_PEARL) return;

      p.spigot().setCollidesWithEntities(false);
      proj.setPassenger(p);
      p.updateInventory();
    }
*/

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDismounted() instanceof Item)) return;
        e.getDismounted().remove();
    }
}