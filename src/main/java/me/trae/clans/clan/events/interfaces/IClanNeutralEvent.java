package me.trae.clans.clan.events.interfaces;

import me.trae.clans.clan.events.ClanNeutralEvent;

public interface IClanNeutralEvent {

    ClanNeutralEvent.Result getResult();
}