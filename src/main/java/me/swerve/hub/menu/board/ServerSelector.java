package me.swerve.hub.menu.board;

import me.swerve.hub.RiseHub;
import me.swerve.hub.manager.BungeeManager;
import me.swerve.hub.menu.Menu;
import me.swerve.hub.menu.Page;
import me.swerve.hub.util.ItemCreator;
import me.swerve.hub.util.Pair;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ServerSelector extends Menu {

    Page meetupPage = new Page(18);

    public ServerSelector(Player p) {
        super("Server Selector", InventoryType.INTERACTABLE, PageInformation.SINGLE_PAGE);

        Page mainPage = new Page(27); // 12, 14

        for (int i = 0; i < 27; i++) mainPage.getPageContents().put(i, new ItemCreator(Material.STAINED_GLASS_PANE, 1).setData(7).getItem());

        mainPage.getPageContents().put(12, new ItemCreator(Material.GOLDEN_APPLE, 1).setName("&6UHC").addLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&7Click to Join UHC"))).getItem());
        mainPage.getPageContents().put(14, new ItemCreator(Material.FISHING_ROD, 1).setName("&6Meetup").addLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&7Click to Join Meetup"))).getItem());

        addPage(mainPage);

        updateMeetupPage();
        addPage(meetupPage);

        updateInventory(p);
    }

    public void updateMeetupPage() {
        for (int i = 0; i < 18; i++) meetupPage.getPageContents().put(i, new ItemCreator(Material.STAINED_GLASS_PANE, 1).setData(7).getItem());
        for (int i = 0; i < 9; i++)  meetupPage.getPageContents().put(i, new ItemStack(Material.AIR));

        meetupPage.getPageContents().put(0, new ItemCreator(Material.STAINED_GLASS_PANE, 1).setData(5).setName("&6&lNA UHC Meetup").getItem());

        if (getServerWool("meetup-1") != null) meetupPage.put(1, getServerWool("meetup-1"));
    }
    @Override
    public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) {
        if (e.getCurrentItem() == null) return;
        e.setCancelled(true);

        if (e.getCurrentItem().getType() == Material.GOLDEN_APPLE) {
            sendToServer(e.getWhoClicked().getUniqueId(), "uhc-1");
            ((Player) e.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSending you to UHC-1..."));
            e.getWhoClicked().closeInventory();
            return;
        }

        if (e.getCurrentItem().getType() == Material.FISHING_ROD) {
            updateMeetupPage();
            setCurrentPage(1);
            updateInventory((Player) e.getWhoClicked());
            return;
        }

        if (e.getCurrentItem().getType() == Material.WOOL) {
            sendToServer(e.getWhoClicked().getUniqueId(), ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).toLowerCase().replace(' ', '-'));
            e.getWhoClicked().closeInventory();
            ((Player) e.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSending you to " + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())));
        }
    }

    private void sendToServer(UUID uuid, String serverName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);
        } catch (Exception ignored) {}

        Bukkit.getPlayer(uuid).sendPluginMessage(RiseHub.getInstance(), "BungeeCord", b.toByteArray());
    }
    public ItemStack getServerWool(String serverName) {
        Pair woolData = getWoolData(serverName);
        if ((int) woolData.getValueOne() == -1) return null;

        String woolName = "&6&l" + StringUtils.capitalize(serverName.replaceAll("-", " "));
        List<String> lore = new ArrayList<>(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', (String) woolData.getValueTwo())));
        lore.add("&7Online Players: " + BungeeManager.getInstance().getOnline(serverName));

        return new ItemCreator(Material.WOOL, 1).setData((int) woolData.getValueOne()).setName(ChatColor.translateAlternateColorCodes('&', woolName)).addLore(lore).getItem();
    }

    public Pair getWoolData(String serverName) {
        String state;
        try (Jedis jedis = RiseHub.getInstance().getPool().getResource()) {
            state = jedis.get(serverName).toUpperCase();
        } catch (Exception e) { return new Pair(-1,-1); }

        int woolID = -1;
        String lore = "";
        switch (state) {
            case "WAITING":
                woolID = 5;
                lore = "&7Waiting for more players..";
                break;
            case "STARTING":
                woolID = 4;
                lore = "&7Starting soon!";
                break;
            case "PLAYING":
                woolID = 10;
                lore = "&7Current playing.. Click to spectate!";
                break;
            case "ENDING":
                woolID = 14;
                lore = "&7Winner! Server will restart soon.";
                break;
            default:
                return new Pair(-1, -1);
        }

        return new Pair(woolID, lore);
    }
    @Override public void lastChance(Inventory inventory) { }

    @Override public void onClose(InventoryCloseEvent e) {}
}



