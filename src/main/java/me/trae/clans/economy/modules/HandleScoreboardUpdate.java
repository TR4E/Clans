package me.trae.clans.economy.modules;

import me.trae.clans.economy.EconomyManager;
import me.trae.clans.economy.events.abstracts.EconomyEvent;
import me.trae.core.framework.types.SpigotListener;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleScoreboardUpdate extends SpigotListener<EconomyManager> {

    public HandleScoreboardUpdate(final EconomyManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEconomy(final EconomyEvent event) {
        if (event.getSender() instanceof Player) {
            UtilServer.callEvent(new ScoreboardUpdateEvent(event.getSenderCasted(Player.class)));
        }

        UtilServer.callEvent(new ScoreboardUpdateEvent(event.getTarget()));
    }
}