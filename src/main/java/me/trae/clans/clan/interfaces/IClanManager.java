package me.trae.clans.clan.interfaces;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.client.Client;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
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

    Clan searchClan(final Player player, final String name, final boolean inform);

    ClanRelation getClanRelationByClan(final Clan clan, final Clan target);

    ClanRelation getClanRelationByPlayer(final Player player, final Player target);

    String getClanName(final Clan clan, final ClanRelation clanRelation);

    String getClanFullName(final Clan clan, final ClanRelation clanRelation);

    String getClanShortName(final Clan clan, final ClanRelation clanRelation);

    void disbandClan(final Clan clan);

    LinkedHashMap<String, String> getClanInformation(final Player player, final Client client, final Clan playerClan, final Clan targetClan);
}