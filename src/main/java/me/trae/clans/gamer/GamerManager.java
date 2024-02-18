package me.trae.clans.gamer;

import me.trae.core.framework.SpigotPlugin;
import me.trae.core.gamer.LocalGamerManager;

import java.util.UUID;

public class GamerManager extends LocalGamerManager<Gamer> {

    public GamerManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        super.registerModules();
    }

    @Override
    public Gamer createGamer(final UUID uuid) {
        return new Gamer(uuid);
    }
}