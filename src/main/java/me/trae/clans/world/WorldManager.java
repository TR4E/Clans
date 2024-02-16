package me.trae.clans.world;

import me.trae.clans.world.commands.TrackCommand;
import me.trae.clans.world.modules.SpringBlock;
import me.trae.clans.world.modules.WaterBlock;
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

        // Core
        addModule(new DisableSaturation(this));
        addModule(new DisableWeather(this));

        // Clans
        addModule(new TrackCommand(this));

        addModule(new SpringBlock(this));
        addModule(new WaterBlock(this));
    }
}