package ar.com.histeriaservers.OnlineDonators;

import ar.com.histeriaservers.OnlineDonators.listeners.PlayerQuitListener;
import net.milkbowl.vault.permission.Permission;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main extends JavaPlugin {

    private List<String> players;
    private BukkitRunnable runnable;
    private Permission vaultPermissionManager;
    
    public Main() {
        this.players = new ArrayList<String>();
    }

    public void onEnable() {
        if (this.setupPermissions()) {
            this.getLogger().info("Vault permissions support hooked!");
        }
        else {
            this.getLogger().severe("Vault permissions support not found!");
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
        }
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerQuitListener(this), (Plugin)this);
        (this.runnable = new BukkitRunnable() {
            public void run() {
                Main.this.players.clear();
                for (final Player online : Main.this.getServer().getOnlinePlayers()) {
                    if (Main.this.getVaultPermissionManager().playerInGroup(online, "RANK1")
                    || Main.this.getVaultPermissionManager().playerInGroup(online, "RANK2")
                    || Main.this.getVaultPermissionManager().playerInGroup(online, "RANK3") // you can add more ranks adding more lines, its easy.
                    || Main.this.getVaultPermissionManager().playerInGroup(online, "RANK4")) {
                        Main.this.players.add(online.getName());
                    }
                }
                if (!Main.this.players.isEmpty()) {
                    Main.this.getServer().broadcastMessage("");
                    Main.this.getServer().broadcastMessage("§e§lOnline donators");
                    Main.this.getServer().broadcastMessage("87» §f" + StringUtils.join((Collection) Main.this.players, ", "));
                    Main.this.getServer().broadcastMessage("");
                    Main.this.getServer().broadcastMessage("§7§oIf you're interested in appearing here, you can buy a rank at §e§ostore.example.com");
                    Main.this.getServer().broadcastMessage("");
                }
            }
        }).runTaskTimerAsynchronously((Plugin)this, 600L, 12000L);
    }
    
    public void onDisable() {
        this.players.clear();
        this.runnable.cancel();
    }
    
    public List<String> getPlayers() {
        return this.players;
    }
    
    private boolean setupPermissions() {
        final RegisteredServiceProvider<Permission> rsp = (RegisteredServiceProvider<Permission>)this.getServer().getServicesManager().getRegistration((Class)Permission.class);
        this.vaultPermissionManager = (Permission)rsp.getProvider();
        return this.vaultPermissionManager != null;
    }
    
    public Permission getVaultPermissionManager() {
        return this.vaultPermissionManager;
    }
}
