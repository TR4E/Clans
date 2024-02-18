package me.trae.clans.clan;

import me.trae.clans.clan.data.Alliance;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.data.Member;
import me.trae.clans.clan.data.Pillage;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.interfaces.IClan;
import me.trae.core.utility.UtilChunk;
import me.trae.core.utility.UtilLocation;
import me.trae.framework.shared.utility.enums.ChatColor;
import me.trae.framework.shared.utility.objects.Data;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Clan implements IClan {

    private final String name;

    private final List<String> territory = new ArrayList<>();

    private final Map<UUID, Member> members = new HashMap<>();
    private final Map<String, Alliance> alliances = new HashMap<>();
    private final Map<String, Enemy> enemies = new HashMap<>();
    private final Map<String, Pillage> pillages = new HashMap<>();

    private long created;
    private UUID founder;
    private Location home;

    public Clan(final String name) {
        this.name = name;
    }

    public Clan(final String name, final Player player) {
        this(name);

        this.addMember(new Member(player, MemberRole.LEADER));

        this.created = System.currentTimeMillis();
        this.founder = player.getUniqueId();
    }

    public Clan(final Data data) {
        this(data.get(String.class, "Key"));

        this.created = data.get(Long.class, "Created");
        this.founder = UUID.fromString(data.get(String.class, "Founder"));
        this.home = UtilLocation.fileToLocation(data.get(String.class, "Home"));
    }

    @Override
    public boolean isAdmin() {
        return this instanceof AdminClan;
    }

    @Override
    public String getType() {
        return "Clan";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getTerritory() {
        return this.territory;
    }

    @Override
    public void addTerritory(final Chunk chunk) {
        this.getTerritory().add(UtilChunk.chunkToFile(chunk));
    }

    @Override
    public void removeTerritory(final Chunk chunk) {
        this.getTerritory().remove(UtilChunk.chunkToFile(chunk));
    }

    @Override
    public boolean isTerritory(final Chunk chunk) {
        return this.getTerritory().contains(UtilChunk.chunkToFile(chunk));
    }

    @Override
    public boolean hasTerritory() {
        return !(this.getTerritory().isEmpty());
    }

    @Override
    public List<Chunk> getTerritoryChunks() {
        final List<Chunk> list = new ArrayList<>();

        for (final String string : this.getTerritory()) {
            final Chunk chunk = UtilChunk.fileToChunk(string);
            if (chunk == null) {
                continue;
            }

            list.add(chunk);
        }

        return list;
    }

    @Override
    public Map<UUID, Member> getMembers() {
        return this.members;
    }

    @Override
    public void addMember(final Member member) {
        this.getMembers().put(member.getUUID(), member);
    }

    @Override
    public void removeMember(final Member member) {
        this.getMembers().remove(member.getUUID());
    }

    @Override
    public Member getMemberByUUID(final UUID uuid) {
        return this.getMembers().getOrDefault(uuid, null);
    }

    @Override
    public Member getMemberByPlayer(final Player player) {
        return this.getMemberByUUID(player.getUniqueId());
    }

    @Override
    public boolean isMemberByUUID(final UUID uuid) {
        return this.getMembers().containsKey(uuid);
    }

    @Override
    public boolean isMemberByPlayer(final Player player) {
        return this.isMemberByUUID(player.getUniqueId());
    }

    @Override
    public Map<String, Alliance> getAlliances() {
        return this.alliances;
    }

    @Override
    public void addAlliance(final Alliance alliance) {
        this.getAlliances().put(alliance.getName(), alliance);
    }

    @Override
    public void removeAlliance(final Alliance alliance) {
        this.getAlliances().remove(alliance.getName());
    }

    @Override
    public Alliance getAllianceByClan(final Clan clan) {
        return this.getAlliances().getOrDefault(clan.getName(), null);
    }

    @Override
    public boolean isAllianceByClan(final Clan clan) {
        return this.getAlliances().containsKey(clan.getName());
    }

    @Override
    public Map<String, Enemy> getEnemies() {
        return this.enemies;
    }

    @Override
    public void addEnemy(final Enemy enemy) {
        this.getEnemies().put(enemy.getName(), enemy);
    }

    @Override
    public void removeEnemy(final Enemy enemy) {
        this.getEnemies().remove(enemy.getName());
    }

    @Override
    public Enemy getEnemyByClan(final Clan clan) {
        return this.getEnemies().getOrDefault(clan.getName(), null);
    }

    @Override
    public boolean isEnemyByClan(final Clan clan) {
        return this.getEnemies().containsKey(clan.getName());
    }

    @Override
    public Map<String, Pillage> getPillages() {
        return this.pillages;
    }

    @Override
    public void addPillage(final Pillage pillage) {
        this.getPillages().put(pillage.getName(), pillage);
    }

    @Override
    public void removePillage(final Pillage pillage) {
        this.getPillages().remove(pillage.getName());
    }

    @Override
    public Pillage getPillageByClan(final Clan clan) {
        return this.getPillages().getOrDefault(clan.getName(), null);
    }

    @Override
    public boolean isPillageByClan(final Clan clan) {
        return this.getPillages().containsKey(clan.getName());
    }

    @Override
    public long getCreated() {
        return this.created;
    }

    @Override
    public UUID getFounder() {
        return this.founder;
    }

    @Override
    public Location getHome() {
        return this.home;
    }

    @Override
    public void setHome(final Location home) {
        this.home = home;
    }

    @Override
    public String getHomeString() {
        if (this.hasHome()) {
            return UtilLocation.locationToString(this.getHome());
        }

        return ChatColor.RED + "Not set";
    }

    @Override
    public boolean hasHome() {
        return this.getHome() != null;
    }
}