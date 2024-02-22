package me.trae.clans.worldevent;

import me.trae.clans.worldevent.interfaces.IWorldEvent;
import me.trae.core.framework.SpigotModule;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilServer;

public abstract class WorldEvent extends SpigotModule<WorldEventManager> implements IWorldEvent {

    private long systemTime;

    public WorldEvent(final WorldEventManager manager) {
        super(manager);
    }

    @Override
    public void start() {
        this.systemTime = System.currentTimeMillis();

        UtilServer.getOnlinePlayers().forEach(player -> UtilServer.callEvent(new ScoreboardUpdateEvent(player)));
    }

    @Override
    public void stop() {
        this.systemTime = 0L;

        UtilServer.getOnlinePlayers().forEach(player -> UtilServer.callEvent(new ScoreboardUpdateEvent(player)));
    }

    @Override
    public long getSystemTime() {
        return this.systemTime;
    }

    @Override
    public boolean isActive() {
        return this.getSystemTime() != 0L;
    }
}