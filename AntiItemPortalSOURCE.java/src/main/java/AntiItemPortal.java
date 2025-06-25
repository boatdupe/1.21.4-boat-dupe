package your.plugin.namespace;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiItemPortal extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("AntiItemPortal enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("AntiItemPortal disabled");
    }

    @EventHandler
    public void onItemPortal(EntityPortalEvent event) {
        if (event.getEntity() instanceof Item) {
            event.setCancelled(true);
        }
    }
}
