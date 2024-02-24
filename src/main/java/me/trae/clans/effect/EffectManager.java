package me.trae.clans.effect;

import me.trae.champions.effect.types.Silenced;
import me.trae.core.framework.SpigotPlugin;

public class EffectManager extends me.trae.core.effect.EffectManager {

    public EffectManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        addModule(new Silenced(this));
    }
}