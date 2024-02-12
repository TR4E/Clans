package me.trae.clans.clan.interfaces;

import me.trae.clans.clan.Clan;

import java.util.Map;

public interface IClanManager {

    Map<String, Clan> getClans();

    void addClan(final Clan clan);

    void removeClan(final Clan clan);

    Clan getClanByName(final String name);

    boolean isClanByName(final String name);
}