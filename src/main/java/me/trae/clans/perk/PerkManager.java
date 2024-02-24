package me.trae.clans.perk;

import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.clans.perk.perks.RaveArmour;
import me.trae.core.framework.SpigotPlugin;

public class PerkManager extends me.trae.core.perk.PerkManager {

    public PerkManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        addModule(new AgilityHelmet(this));
        addModule(new RaveArmour(this));
    }
}