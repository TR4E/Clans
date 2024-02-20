package me.trae.clans.perk.perks;

import me.trae.core.perk.Perk;
import me.trae.core.perk.PerkManager;
import me.trae.framework.shared.perk.PerkAttributes;

public class RaveArmour extends Perk implements PerkAttributes {

    public RaveArmour(final PerkManager manager) {
        super(manager);
    }

    @Override
    public boolean isAutoClaim() {
        return false;
    }

    @Override
    public long getDuration() {
        return 0;
    }
}