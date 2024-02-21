package me.trae.clans.pillage.events.abstracts;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.Pillage;
import me.trae.clans.pillage.events.abstracts.interfaces.IPillageEvent;
import me.trae.core.event.CustomEvent;

public class PillageEvent extends CustomEvent implements IPillageEvent {

    private final Clan pillagerClan, pillageeClan;
    private final Pillage pillage;

    public PillageEvent(final Clan pillagerClan, final Clan pillageeClan, final Pillage pillage) {
        this.pillagerClan = pillagerClan;
        this.pillageeClan = pillageeClan;
        this.pillage = pillage;
    }

    @Override
    public Clan getPillagerClan() {
        return this.pillagerClan;
    }

    @Override
    public Clan getPillageeClan() {
        return this.pillageeClan;
    }

    @Override
    public Pillage getPillage() {
        return this.pillage;
    }
}