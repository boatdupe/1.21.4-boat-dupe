package com.alphasilk.portalboatblocker.paper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ChestBoat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class PaperPlugin extends JavaPlugin implements Listener {

    private final Set<UUID> portalFlaggedBoats = Collections.synchronizedSet(new HashSet<>());
    private Logger logger;

    @Override
    public void onEnable() {
        this.logger = getLogger();
        Bukkit.getPluginManager().registerEvents(this, this);
        logger.info("[PortalBoatBlocker] Paper plugin enabled.");
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        if (event.getEntity() instanceof ChestBoat chestBoat) {
            // Cancel the teleport and flag the boat
            event.setCancelled(true);
            portalFlaggedBoats.add(chestBoat.getUniqueId());
            logger.info("[PortalBoatBlocker] Chest boat attempted to teleport. Teleport blocked and flagged.");
        }
    }

    @EventHandler
    public void onBoatBreak(VehicleDestroyEvent event) {
        if (event.getVehicle() instanceof ChestBoat chestBoat) {
            UUID id = chestBoat.getUniqueId();
            if (portalFlaggedBoats.contains(id)) {
                // Drop duplicated contents
                Location loc = chestBoat.getLocation();
                chestBoat.getInventory().forEach(item -> {
                    if (item != null && item.getType().isItem()) {
                        loc.getWorld().dropItemNaturally(loc, item.clone());
                    }
                });
                logger.info("[PortalBoatBlocker] Flagged chest boat broken. Contents duplicated.");
                portalFlaggedBoats.remove(id); // Clean up flag
            }
        }
    }
}
// Paper plugin entry point
