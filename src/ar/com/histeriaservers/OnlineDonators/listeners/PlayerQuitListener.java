package ar.com.histeriaservers.OnlineDonators.listeners;

import ar.com.histeriaservers.OnlineDonators.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener
{
    private Main plugin;
    
    public PlayerQuitListener(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (this.plugin.getPlayers().contains(event.getPlayer().getName())) {
            this.plugin.getPlayers().remove(event.getPlayer().getName());
        }
    }
}
