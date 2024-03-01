package me.trae.clans.clan.modules.scoreboard;

import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.events.abstracts.types.player.ClanPlayerCancellableEvent;
import me.trae.clans.clan.events.abstracts.types.player.ClanPlayerEvent;
import me.trae.core.framework.types.SpigotListener;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleScoreboardUpdate extends SpigotListener<ClanManager> {

    public HandleScoreboardUpdate(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanPlayer(final ClanPlayerEvent event) {
        UtilServer.callEvent(new ScoreboardUpdateEvent(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanPlayerCancellable(final ClanPlayerCancellableEvent event) {
        if (event.isCancelled()) {
            return;
        }

        UtilServer.callEvent(new ScoreboardUpdateEvent(event.getPlayer()));
    }
}