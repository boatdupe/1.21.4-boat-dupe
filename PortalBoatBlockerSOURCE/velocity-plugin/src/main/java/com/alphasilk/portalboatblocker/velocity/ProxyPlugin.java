package com.alphasilk.portalboatblocker.proxy;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Plugin(id = "portalboatblocker", name = "PortalBoatBlocker", version = "1.0.0", authors = {"Alphasilk"})
public class ProxyPlugin {

    private final ProxyServer server;
    private final Logger logger;
    private final Set<UUID> throttledPlayers = new HashSet<>();

    @Inject
    public ProxyPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        Player player = event.getPlayer();

        server.getScheduler().buildTask(this, () -> {
            if (player.isActive() && player.getCurrentServer().isPresent()) {
                if (throttledPlayers.add(player.getUniqueId())) {
                    logger.info("(Placeholder) Would desync chest boat movement for " + player.getUsername());
                }
            }
        }).repeat(1, TimeUnit.SECONDS).schedule();
    }
}
