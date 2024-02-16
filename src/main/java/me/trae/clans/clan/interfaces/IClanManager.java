package me.trae.clans.clan.interfaces;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.enums.ClanRelation;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public interface IClanManager {

    Map<String, Clan> getClans();

    void addClan(final Clan clan);

    void removeClan(final Clan clan);

    Clan getClanByName(final String name);

    Clan getClanByUUID(final UUID uuid);

    Clan getClanByPlayer(final Player player);

    Clan getClanByChunk(final Chunk chunk);

    Clan getClanByLocation(final Location location);

    boolean isClanByName(final String name);

    boolean isClanByUUID(final UUID uuid);

    boolean isClanByPlayer(final Player player);

    boolean isClanByChunk(final Chunk chunk);

    boolean isClanByLocation(final Location location);

    ClanRelation getClanRelationByClan(final Clan clan, final Clan target);

    ClanRelation getClanRelationByPlayer(final Player player, final Player target);
}