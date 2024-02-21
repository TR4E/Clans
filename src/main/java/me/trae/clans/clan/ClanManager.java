package me.trae.clans.clan;

import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.data.Pillage;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.interfaces.IClanManager;
import me.trae.clans.clan.modules.chat.HandleChatReceiver;
import me.trae.clans.clan.modules.death.HandleCustomDeathMessageReceiver;
import me.trae.clans.clan.modules.scoreboard.HandleScoreboardUpdate;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.SpigotManager;
import me.trae.core.framework.SpigotPlugin;
import me.trae.core.utility.UtilSearch;
import me.trae.framework.shared.database.repository.interfaces.RepositoryContainer;
import me.trae.framework.shared.utility.enums.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ClanManager extends SpigotManager implements IClanManager, RepositoryContainer<ClanRepository> {

    private final Map<String, Clan> CLANS = new HashMap<>();

    public ClanManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new ClanCommand(this));

        // Chat Modules
        addModule(new HandleChatReceiver(this));

        // Death Modules
        addModule(new HandleCustomDeathMessageReceiver(this));

        // Scoreboard Modules
        addModule(new HandleScoreboardUpdate(this));
    }

    @Override
    public Class<ClanRepository> getClassOfRepository() {
        return ClanRepository.class;
    }

    @Override
    public Map<String, Clan> getClans() {
        return this.CLANS;
    }

    @Override
    public void addClan(final Clan clan) {
        this.getClans().put(clan.getName().toLowerCase(), clan);
    }

    @Override
    public void removeClan(final Clan clan) {
        this.getClans().remove(clan.getName().toLowerCase());
    }

    @Override
    public Clan getClanByName(final String name) {
        return this.getClans().getOrDefault(name.toLowerCase(), null);
    }

    @Override
    public Clan getClanByUUID(final UUID uuid) {
        for (final Clan clan : this.getClans().values()) {
            if (!(clan.isMemberByUUID(uuid))) {
                continue;
            }

            return clan;
        }

        return null;
    }

    @Override
    public Clan getClanByPlayer(final Player player) {
        return this.getClanByUUID(player.getUniqueId());
    }

    @Override
    public Clan getClanByChunk(final Chunk chunk) {
        for (final Clan clan : this.getClans().values()) {
            if (!(clan.isTerritory(chunk))) {
                continue;
            }

            return clan;
        }

        return null;
    }

    @Override
    public Clan getClanByLocation(final Location location) {
        return this.getClanByChunk(location.getChunk());
    }

    @Override
    public boolean isClanByName(final String name) {
        return this.getClans().containsKey(name.toLowerCase());
    }

    @Override
    public boolean isClanByUUID(final UUID uuid) {
        return this.getClanByUUID(uuid) != null;
    }

    @Override
    public boolean isClanByPlayer(final Player player) {
        return this.getClanByPlayer(player) != null;
    }

    @Override
    public boolean isClanByChunk(final Chunk chunk) {
        return this.getClanByChunk(chunk) != null;
    }

    @Override
    public boolean isClanByLocation(final Location location) {
        return this.getClanByLocation(location) != null;
    }

    @Override
    public Clan searchClan(final Player player, final String name, final boolean inform) {
        final List<Predicate<Clan>> predicates = Arrays.asList(
                (clan -> clan.getName().equalsIgnoreCase(name)),
                (clan -> clan.getName().toLowerCase().contains(name.toLowerCase()))
        );

        final Function<Clan, String> function = (clan -> this.getClanRelationByClan(this.getClanByPlayer(player), clan).getSuffix() + clan.getName());

        final Consumer<List<Clan>> consumer = (list -> {
            final Client client = this.getInstance().getManagerByClass(ClientManager.class).searchClient(player, name, false);
            if (client == null) {
                return;
            }

            final Clan clan = this.getClanByUUID(client.getUUID());
            if (clan == null) {
                return;
            }

            if (list.contains(clan)) {
                return;
            }

            list.add(clan);
        });

        return UtilSearch.search(Clan.class, this.getClans().values(), predicates, consumer, function, "Clan Search", player, name, inform);
    }

    @Override
    public ClanRelation getClanRelationByClan(final Clan clan, final Clan target) {
        if (clan != null && target != null) {
            if (clan == target) {
                return ClanRelation.SELF;
            }

            if (clan.isAllianceByClan(target)) {
                return clan.getAllianceByClan(target).isTrusted() ? ClanRelation.TRUSTED_ALLIANCE : ClanRelation.ALLIANCE;
            }

            if (clan.isEnemyByClan(target)) {
                return ClanRelation.ENEMY;
            }

            if (clan.isPillageByClan(target) || target.isPillageByClan(clan)) {
                return ClanRelation.PILLAGE;
            }
        }

        return ClanRelation.NEUTRAL;
    }

    @Override
    public ClanRelation getClanRelationByPlayer(final Player player, final Player target) {
        return this.getClanRelationByClan(this.getClanByPlayer(player), this.getClanByPlayer(target));
    }

    @Override
    public String getClanName(final Clan clan, final ClanRelation clanRelation) {
        return clan.isAdmin() ? this.getClanShortName(clan, clanRelation) : this.getClanFullName(clan, clanRelation);
    }

    @Override
    public String getClanFullName(final Clan clan, final ClanRelation clanRelation) {
        return clanRelation.getSuffix() + String.format("%s %s", clan.getType(), clan.getName());
    }

    @Override
    public String getClanShortName(final Clan clan, final ClanRelation clanRelation) {
        ChatColor chatColor = clanRelation.getSuffix();

        if (clan.isAdmin()) {
            chatColor = ChatColor.WHITE;
        }

        return chatColor + clan.getName();
    }

    @Override
    public void disbandClan(final Clan clan) {
        // TODO: 18/02/2024 - Iterate through claims and get rid of Chunk Outlines

        for (final Clan target : this.getClans().values()) {
            if (target.isAllianceByClan(clan)) {
                target.removeAlliance(target.getAllianceByClan(clan));
            }

            if (target.isEnemyByClan(clan)) {
                target.removeEnemy(target.getEnemyByClan(clan));
            }

            if (target.isPillageByClan(clan)) {
                final Pillage pillage = target.getPillageByClan(clan);

                target.removePillage(pillage);

                // TODO: 18/02/2024 - Call PillageEndEvent
            }
        }

        this.removeClan(clan);
        this.getRepository().deleteData(clan);
    }

    @Override
    public LinkedHashMap<String, String> getClanInformation(final Player player, final Client client, final Clan playerClan, final Clan targetClan) {
        final LinkedHashMap<String, String> map = new LinkedHashMap<>();

        if (client.isAdministrating()) {
            map.put("Founder", String.format("<yellow>%s", targetClan.getFounderString()));
        }

        map.put("Age", String.format("<yellow>%s", targetClan.getCreatedString()));
        map.put("Territory", String.format("<yellow>%s", targetClan.getTerritoryString()));

        if (targetClan.isMemberByPlayer(player) || client.isAdministrating()) {
            map.put("Home", targetClan.getHomeString());
        }

        map.put("Allies", targetClan.getAlliancesString(this, playerClan));
        map.put("Enemies", targetClan.getEnemiesString(this, playerClan));
        map.put("Pillages", targetClan.getPillagesString(this, playerClan));
        map.put("Members", targetClan.getMembersString(this, player));


        return map;
    }
}