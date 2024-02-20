package me.trae.clans.damage;

import me.trae.core.damage.modules.HandleDisplayDamageOnEXPLevel;
import me.trae.core.framework.SpigotPlugin;

public class DamageManager extends me.trae.core.damage.DamageManager {

    public DamageManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        super.registerModules();

        addModule(new HandleDisplayDamageOnEXPLevel(this));
    }
}