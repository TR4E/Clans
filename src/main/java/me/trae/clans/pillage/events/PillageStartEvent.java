package me.trae.clans.pillage.events;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.Pillage;
import me.trae.clans.pillage.events.abstracts.PillageEvent;

public class PillageStartEvent extends PillageEvent {

    public PillageStartEvent(final Clan pillagerClan, final Clan pillageeClan, final Pillage pillage) {
        super(pillagerClan, pillageeClan, pillage);
    }
}