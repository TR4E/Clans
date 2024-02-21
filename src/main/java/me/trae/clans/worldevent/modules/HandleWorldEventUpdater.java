package me.trae.clans.worldevent.modules;

import me.trae.clans.worldevent.WorldEvent;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.core.framework.types.SpigotUpdater;
import me.trae.framework.shared.updater.annotations.Update;
import me.trae.framework.shared.utility.UtilTime;

public class HandleWorldEventUpdater extends SpigotUpdater<WorldEventManager> {

    public HandleWorldEventUpdater(final WorldEventManager manager) {
        super(manager);
    }

    @Update
    public void onUpdate() {
        for (final WorldEvent worldEvent : this.getManager().getModulesByClass(WorldEvent.class)) {
            if (!(worldEvent.isActive())) {
                continue;
            }

            if (!(UtilTime.elapsed(worldEvent.getSystemTime(), worldEvent.getDuration()))) {
                continue;
            }

            worldEvent.stop();
        }
    }
}