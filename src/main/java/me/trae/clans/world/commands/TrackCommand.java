package me.trae.clans.world.commands;

import me.trae.clans.world.WorldManager;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.command.types.PlayerCommand;
import me.trae.core.item.events.ItemUpdateEvent;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import me.trae.framework.shared.client.enums.Rank;
import me.trae.framework.shared.updater.annotations.Update;
import me.trae.framework.shared.updater.interfaces.Updater;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class TrackCommand extends PlayerCommand<WorldManager> implements Updater, Listener {

    private final Map<UUID, UUID> MAP = new HashMap<>();

    public TrackCommand(final WorldManager manager) {
        super(manager, "track", new String[]{"find", "locate"}, Rank.DEFAULT);
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <player>";
    }

    @Override
    public String getDescription() {
        return "Track a Player";
    }

    @Override
    public void execute(final Player player, final Client client, final String[] args) {
        if (args.length == 0) {
            UtilMessage.message(player, "Tracker", "You did not input a Player to Track.");
            return;
        }

        final Player target = UtilPlayer.searchPlayer(player, args[0], true);
        if (target == null) {
            return;
        }

        if (!(this.canTrack(player, target))) {
            return;
        }

        this.MAP.put(player.getUniqueId(), target.getUniqueId());

        UtilMessage.simpleMessage(player, "Tracker", "You are now tracking <yellow><var></yellow>.", Collections.singletonList(target.getName()));
    }

    private boolean canTrack(final Player player, final Player target) {
        if (player == target) {
            UtilMessage.message(player, "Tracker", "You cannot track yourself!");
            return false;
        }

        if (this.getInstance().getManagerByClass(ClientManager.class).getClientByPlayer(target).isAdministrating()) {
            UtilMessage.message(player, "Tracker", "You cannot track this player at this time!");
            return false;
        }

        if (player.getWorld() != target.getWorld()) {
            UtilMessage.message(player, "Tracker", "You cannot track a player that is in another world!");
            return false;
        }

        return true;
    }

    @Update(delay = 125L, asynchronous = true)
    public void onUpdate() {
        this.MAP.entrySet().removeIf(entry -> {
            final Player player = Bukkit.getPlayer(entry.getKey());
            final Player target = Bukkit.getPlayer(entry.getValue());

            if (player == null || target == null) {
                return true;
            }

            player.setCompassTarget(target.getLocation());
            return false;
        });
    }

    @EventHandler
    public void onItemUpdate(final ItemUpdateEvent event) {
        if (event.getBuilder().getItemStack().getType() != Material.COMPASS) {
            return;
        }

        event.getBuilder().setDisplayName("Tracking Device");
        event.getBuilder().setLore(Arrays.asList(" ", String.format("<gray>Usage:</gray> <white>%s</white>", this.getUsage())));
    }
}