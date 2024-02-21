package me.trae.clans.worldevent.types;

import me.trae.clans.worldevent.WorldEvent;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.framework.shared.utility.enums.TimeUnit;

public class MiningFrenzy extends WorldEvent {

    public MiningFrenzy(final WorldEventManager manager) {
        super(manager);
    }

    @Override
    public long getDuration() {
        return TimeUnit.MINUTES.getDuration() * 15;
    }
}