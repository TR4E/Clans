package me.trae.clans.pillage.events.abstracts.interfaces;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.Pillage;

public interface IPillageEvent {

    Clan getPillagerClan();

    Clan getPillageeClan();

    Pillage getPillage();
}