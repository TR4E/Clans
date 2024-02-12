package me.trae.clans.world;

import me.trae.core.framework.SpigotPlugin;
import me.trae.core.world.modules.DisableSaturation;
import me.trae.core.world.modules.DisableWeather;

public class WorldManager extends me.trae.core.world.WorldManager {

    public WorldManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        super.registerModules();

        addModule(new DisableSaturation(this));
        addModule(new DisableWeather(this));
    }
}