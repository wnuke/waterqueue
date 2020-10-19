package dev.wnuke.waterqueue;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class WaterqueueCompanion extends JavaPlugin implements Listener, PluginMessageListener {
    public static WaterqueueCompanion INSTANCE;
    private World end;

    @Override
    public void onEnable() {
        getLogger().info("Loading Waterqueue by wnuke...");
        INSTANCE = this;
        for (World world : getServer().getWorlds()) {
            if (world.getEnvironment() == World.Environment.THE_END) {
                end = world;
                break;
            }
        }
        if (end != null) {
            end.setAmbientSpawnLimit(0);
            end.setAnimalSpawnLimit(0);
            end.setAutoSave(false);
            end.setDifficulty(Difficulty.PEACEFUL);
            end.setMonsterSpawnLimit(0);
            end.setKeepSpawnInMemory(true);
            end.setSpawnLocation(0, 0, 0);
            end.setPVP(false);
        }
        getServer().getPluginManager().registerEvents(this, this);
        getServer().setDefaultGameMode(GameMode.SPECTATOR);
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Placeholders placeholders = new Placeholders();
            placeholders.register();
            getServer().getMessenger().registerIncomingPluginChannel(this, Placeholders.INFO_CHANNEL, placeholders);
        }
        getLogger().info("Waterqueue by wnuke loaded.");
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        for (Player player : getServer().getOnlinePlayers()) {
            player.hidePlayer(this, event.getPlayer());
            event.getPlayer().hidePlayer(this, player);
        }
        event.getPlayer().setGameMode(GameMode.SPECTATOR);
        event.getPlayer().teleport(end.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.PLUGIN) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

    }
}
