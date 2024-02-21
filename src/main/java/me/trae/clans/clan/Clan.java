package me.trae.clans.clan;

import me.trae.clans.clan.data.Alliance;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.data.Member;
import me.trae.clans.clan.data.Pillage;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.interfaces.IClan;
import me.trae.core.utility.UtilChunk;
import me.trae.core.utility.UtilLocation;
import me.trae.framework.shared.utility.UtilJava;
import me.trae.framework.shared.utility.UtilTime;
import me.trae.framework.shared.utility.enums.ChatColor;
import me.trae.framework.shared.utility.interfaces.property.PropertyContainer;
import me.trae.framework.shared.utility.objects.EnumData;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Clan implements IClan, PropertyContainer<ClanProperty> {

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

    public Clan(final EnumData<ClanProperty> data) {
        this(data.get(String.class, "Key"));

        data.getList(String.class, ClanProperty.TERRITORY).forEach(string -> this.addTerritory(UtilChunk.fileToChunk(string)));

        data.getList(String.class, ClanProperty.MEMBERS).forEach(string -> this.addMember(new Member(string.split(":"))));
        data.getList(String.class, ClanProperty.ALLIANCES).forEach(string -> this.addAlliance(new Alliance(string.split(":"))));
        data.getList(String.class, ClanProperty.ENEMIES).forEach(string -> this.addEnemy(new Enemy(string.split(":"))));
        data.getList(String.class, ClanProperty.PILLAGES).forEach(string -> this.addPillage(new Pillage(string.split(":"))));

        this.created = data.get(Long.class, ClanProperty.CREATED);
        this.founder = UUID.fromString(data.get(String.class, ClanProperty.FOUNDER));
        this.home = UtilLocation.fileToLocation(data.get(String.class, ClanProperty.HOME));
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
    public int getMaxClaims() {
        return 3 + this.getMembers().size();
    }

    @Override
    public String getTerritoryString() {
        return String.format("%s/%s", this.getTerritory().size(), this.getMaxClaims());
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
    public String getMembersString(final ClanManager manager, final Player receiverPlayer) {
        final List<String> list = new ArrayList<>();

        for (final Member member : this.getMembers().values()) {
            String name = "null";
            ChatColor chatColor = ChatColor.RED;

            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(member.getUUID());
            if (offlinePlayer != null) {
                name = offlinePlayer.getName();

                if (offlinePlayer.isOnline() && receiverPlayer.canSee(offlinePlayer.getPlayer())) {
                    chatColor = ChatColor.GREEN;
                }
            }

            list.add(String.format("<yellow>%s</yellow>.%s", member.getRole().getPrefix(), chatColor + name));
        }

        return String.join("<gray>,", list);
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
    public String getAlliancesString(final ClanManager manager, final Clan receiverClan) {
        final List<String> list = new ArrayList<>();

        for (final Alliance alliance : this.getAlliances().values()) {
            final Clan allianceClan = manager.getClanByName(alliance.getName());
            if (allianceClan == null) {
                continue;
            }

            final ClanRelation clanRelation = manager.getClanRelationByClan(receiverClan, allianceClan);

            list.add(clanRelation.getSuffix() + allianceClan.getName());
        }

        return String.join("<gray>,", list);
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
    public String getEnemiesString(final ClanManager manager, final Clan receiverClan) {
        final List<String> list = new ArrayList<>();

        for (final Enemy enemy : this.getEnemies().values()) {
            final Clan enemyClan = manager.getClanByName(enemy.getName());
            if (enemyClan == null) {
                continue;
            }

            final ClanRelation clanRelation = manager.getClanRelationByClan(receiverClan, enemyClan);

            list.add(clanRelation.getSuffix() + enemyClan.getName());
        }

        return String.join("<gray>,", list);
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
    public String getPillagesString(final ClanManager manager, final Clan receiverClan) {
        final List<String> list = new ArrayList<>();

        for (final Pillage pillage : this.getPillages().values()) {
            final Clan pillageClan = manager.getClanByName(pillage.getName());
            if (pillageClan == null) {
                continue;
            }

            final ClanRelation clanRelation = manager.getClanRelationByClan(receiverClan, pillageClan);

            list.add(clanRelation.getSuffix() + pillageClan.getName());
        }

        return String.join("<gray>,", list);
    }

    @Override
    public long getCreated() {
        return this.created;
    }

    @Override
    public String getCreatedString() {
        return UtilTime.getTime(System.currentTimeMillis() - this.getCreated());
    }

    @Override
    public UUID getFounder() {
        return this.founder;
    }

    @Override
    public String getFounderString() {
        return Bukkit.getOfflinePlayer(this.getFounder()).getName();
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

    @Override
    public Object getPropertyByValue(final ClanProperty property) {
        switch (property) {
            case TERRITORY:
                return new ArrayList<>(this.getTerritory());
            case MEMBERS:
                return this.getMembers().values().stream().map(Member::toString).collect(Collectors.toList());
            case ALLIANCES:
                return this.getAlliances().values().stream().map(Alliance::toString).collect(Collectors.toList());
            case ENEMIES:
                return this.getEnemies().values().stream().map(Enemy::toString).collect(Collectors.toList());
            case PILLAGES:
                return this.getPillages().values().stream().map(Pillage::toString).collect(Collectors.toList());
            case CREATED:
                return this.getCreated();
            case FOUNDER:
                return this.getFounder().toString();
            case HOME:
                return UtilLocation.locationToFile(this.getHome());
            case ADMIN:
                return this.isAdmin();
            case SAFE:
                return this.isAdmin() && UtilJava.cast(AdminClan.class, this).isSafe();
        }

        return null;
    }

    @Override
    public List<ClanProperty> getProperties() {
        return Arrays.asList(ClanProperty.values());
    }
}