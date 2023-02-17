package me.swerve.hub.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherListener implements Listener {

    @EventHandler
    public void onWeather(WeatherChangeEvent e) {
        if (e.toWeatherState()) e.setCancelled(true);
    }
}
