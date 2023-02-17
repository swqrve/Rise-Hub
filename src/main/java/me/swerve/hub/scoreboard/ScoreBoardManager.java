package me.swerve.hub.scoreboard;

import assemble.AssembleAdapter;
import me.swerve.hub.manager.BungeeManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreBoardManager implements AssembleAdapter {

    public String getTitle(Player player) { return ChatColor.translateAlternateColorCodes('&', "&6&lRise Network");
    }

    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7&m-------------------------------");
        lines.add("&6&lInfo:");

        lines.add("&6Rank&f: &fDefault");// TODO: Get Rank from RANKS API

        AtomicInteger totalOnline = new AtomicInteger();
        BungeeManager.getInstance().getServersOnline().values().forEach(totalOnline::addAndGet);
        lines.add("&6Players: &f" + totalOnline);

        lines.add("");
        lines.add("&7riseuhc.club");
        lines.add("&7&m-------------------------------");

        List<String> toReturn = new ArrayList<>();
        lines.forEach(line -> toReturn.add(ChatColor.translateAlternateColorCodes('&', line)));
        return toReturn;
    }
}
