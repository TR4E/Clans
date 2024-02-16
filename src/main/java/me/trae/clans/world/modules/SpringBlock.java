package me.trae.clans.world.modules;

import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.SpigotListener;
import me.trae.core.item.events.ItemUpdateEvent;
import me.trae.core.recharge.RechargeManager;
import me.trae.core.utility.UtilMath;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class SpringBlock extends SpigotListener<WorldManager> {

    public SpringBlock(final WorldManager manager) {
        super(manager);

        this.addPrimitive("Recharge", 800L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block block = event.getClickedBlock();

        if (block.getType() != Material.SPONGE) {
            return;
        }

        final Player player = event.getPlayer();

        if (UtilMath.offset(player.getLocation().toVector(), block.getLocation().add(0.5D, 1.5D, 0.5D).toVector()) > 0.6D) {
            return;
        }

        if (!(this.getInstance().getManagerByClass(RechargeManager.class).add(player, this.getName(), this.getPrimitiveCasted(Long.class, "Recharge"), false))) {
            return;
        }

        event.setCancelled(true);

        player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0, 15);

        for (int i = 0; i < 3; i++) {
            player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, block.getType(), 15);
        }

        player.setVelocity(new Vector(0.0D, 1.8D, 0.0D));
    }

    @EventHandler
    public void onItemUpdate(final ItemUpdateEvent event) {
        if (event.getBuilder().getItemStack().getType() != Material.SPONGE) {
            return;
        }

        event.getBuilder().setDisplayName(this.getName());
    }
}