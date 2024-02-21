package me.trae.clans.world.modules;

import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.SpigotListener;
import me.trae.framework.shared.utility.UtilMath;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class LimitCreatureSpawn extends SpigotListener<WorldManager> {

    public LimitCreatureSpawn(final WorldManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }

        if (UtilMath.getRandomNumber(Integer.class, 100) > 70) {
            return;
        }

        event.setCancelled(true);
    }
}