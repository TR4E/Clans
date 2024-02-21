package me.trae.clans.clan.interfaces;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.data.Alliance;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.data.Member;
import me.trae.clans.clan.data.Pillage;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IClan {

    boolean isAdmin();

    String getType();

    String getName();

    List<String> getTerritory();

    void addTerritory(final Chunk chunk);

    void removeTerritory(final Chunk chunk);

    boolean isTerritory(final Chunk chunk);

    boolean hasTerritory();

    List<Chunk> getTerritoryChunks();

    int getMaxClaims();

    String getTerritoryString();

    Map<UUID, Member> getMembers();

    void addMember(final Member member);

    void removeMember(final Member member);

    Member getMemberByUUID(final UUID uuid);

    Member getMemberByPlayer(final Player player);

    boolean isMemberByUUID(final UUID uuid);

    boolean isMemberByPlayer(final Player player);

    String getMembersString(final ClanManager manager, final Player receiverPlayer);

    Map<String, Alliance> getAlliances();

    void addAlliance(final Alliance alliance);

    void removeAlliance(final Alliance alliance);

    Alliance getAllianceByClan(final Clan clan);

    boolean isAllianceByClan(final Clan clan);

    String getAlliancesString(final ClanManager manager, final Clan receiverClan);

    Map<String, Enemy> getEnemies();

    void addEnemy(final Enemy enemy);

    void removeEnemy(final Enemy enemy);

    Enemy getEnemyByClan(final Clan clan);

    boolean isEnemyByClan(final Clan clan);

    String getEnemiesString(final ClanManager manager, final Clan receiverClan);

    Map<String, Pillage> getPillages();

    void addPillage(final Pillage pillage);

    void removePillage(final Pillage pillage);

    Pillage getPillageByClan(final Clan clan);

    boolean isPillageByClan(final Clan clan);

    String getPillagesString(final ClanManager manager, final Clan receiverClan);

    long getCreated();

    String getCreatedString();

    UUID getFounder();

    String getFounderString();

    Location getHome();

    void setHome(final Location location);

    String getHomeString();

    boolean hasHome();
}