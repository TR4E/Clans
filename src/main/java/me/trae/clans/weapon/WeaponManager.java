package me.trae.clans.weapon;

import me.trae.champions.weapon.*;
import me.trae.core.framework.SpigotPlugin;

public class WeaponManager extends me.trae.core.weapon.WeaponManager {

    public WeaponManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        super.registerModules();

        addModule(new BoosterAxe(this));
        addModule(new BoosterSword(this));
        addModule(new PowerAxe(this));
        addModule(new PowerSword(this));
        addModule(new StandardAxe(this));
        addModule(new StandardSword(this));
    }
}