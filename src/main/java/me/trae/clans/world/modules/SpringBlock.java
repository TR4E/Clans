package me.trae.clans.world.modules;

import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.SpigotListener;
import me.trae.core.item.events.ItemUpdateEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

public class SpringBlock extends SpigotListener<WorldManager> {

    public SpringBlock(final WorldManager manager) {
        super(manager);
    }

    @EventHandler
    public void onItemUpdate(final ItemUpdateEvent event) {
        if (event.getBuilder().getItemStack().getType() != Material.SPONGE) {
            return;
        }

        event.getBuilder().setDisplayName(this.getName());
    }
}