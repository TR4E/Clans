package me.trae.clans.clan.events.interfaces;

import me.trae.clans.clan.Clan;
import me.trae.core.event.types.IChunkEvent;

public interface ITerritoryUnClaimEvent extends IChunkEvent {

    Clan getTerritoryClan();
}