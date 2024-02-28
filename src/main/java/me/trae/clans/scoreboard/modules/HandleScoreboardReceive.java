package me.trae.clans.scoreboard.modules;

import me.trae.clans.scoreboard.ClanScoreboard;
import me.trae.clans.scoreboard.ScoreboardManager;
import me.trae.core.framework.types.SpigotListener;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import org.bukkit.event.EventHandler;

public class HandleScoreboardReceive extends SpigotListener<ScoreboardManager> {

    public HandleScoreboardReceive(final ScoreboardManager manager) {
        super(manager);
    }

    @EventHandler
    public void onScoreboardUpdate(final ScoreboardUpdateEvent event) {
        event.setClassOfScoreboard(ClanScoreboard.class);
    }
}