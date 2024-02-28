package me.trae.clans.scoreboard;

import me.trae.clans.scoreboard.modules.HandleScoreboardReceive;
import me.trae.core.framework.SpigotPlugin;

public class ScoreboardManager extends me.trae.core.scoreboard.ScoreboardManager {

    public ScoreboardManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        addModule(new HandleScoreboardReceive(this));
    }
}