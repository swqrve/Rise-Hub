package me.swerve.hub;

import assemble.Assemble;
import lombok.Getter;
import me.swerve.hub.hotbar.HubHotbar;
import me.swerve.hub.listener.*;
import me.swerve.hub.manager.BungeeManager;
import me.swerve.hub.scoreboard.ScoreBoardManager;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public final class RiseHub extends JavaPlugin {
    @Getter private static RiseHub instance;
    @Getter private JedisPool pool;


    @Override
    public void onEnable() {
        instance = this;

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeManager());

        new HubHotbar();

        registerListeners();

        new Assemble(this, new ScoreBoardManager());

        // Not my fix, this is disgusting please end me
        ClassLoader previous = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(Jedis.class.getClassLoader());
        pool = new JedisPool("127.0.0.1", 6379);
        Thread.currentThread().setContextClassLoader(previous);

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseHub&f] &fRise Hub has been enabled.."));
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseHub&f] &fRise Hub has been disabled.."));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new DoubleJumpListener(), this);
        Bukkit.getPluginManager().registerEvents(new WeatherListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new HungerListener(), this);
        Bukkit.getPluginManager().registerEvents(new DropListener(), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new EnderButtListener(), this);
    }
}