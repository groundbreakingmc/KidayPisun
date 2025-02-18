package com.github.groundbreakingmc.kidaypisun.listeners;

import com.github.groundbreakingmc.kidaypisun.KidayPisun;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class DisconectListener implements Listener {

    private final KidayPisun plugin;

    public DisconectListener(final KidayPisun plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        this.plugin.cancel(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onKick(final PlayerKickEvent event) {
        this.plugin.cancel(event.getPlayer());
    }
}
