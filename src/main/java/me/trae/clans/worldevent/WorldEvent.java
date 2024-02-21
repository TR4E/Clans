package me.trae.clans.worldevent;

import me.trae.clans.worldevent.interfaces.IWorldEvent;
import me.trae.core.framework.SpigotModule;

public abstract class WorldEvent extends SpigotModule<WorldEventManager> implements IWorldEvent {

    private long systemTime;

    public WorldEvent(final WorldEventManager manager) {
        super(manager);
    }

    @Override
    public void start() {
        this.systemTime = System.currentTimeMillis();
    }

    @Override
    public void stop() {
        this.systemTime = 0L;
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