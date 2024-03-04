package me.trae.clans.clan;

import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.chat.AllyChatCommand;
import me.trae.clans.clan.commands.chat.ClanChatCommand;
import me.trae.clans.clan.data.Alliance;
import me.trae.clans.clan.data.Pillage;
import me.trae.clans.clan.enums.ChatType;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.interfaces.IClanManager;
import me.trae.clans.clan.modules.chat.HandleChatReceiver;
import me.trae.clans.clan.modules.death.HandleCustomDeathMessageReceiver;
import me.trae.clans.clan.modules.scoreboard.HandleScoreboardUpdate;
import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.core.blockrestore.BlockRestoreManager;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.SpigotManager;
import me.trae.core.framework.SpigotPlugin;
import me.trae.core.gamer.local.events.ChatTypeUpdateEvent;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilSearch;
import me.trae.core.utility.UtilServer;
import me.trae.framework.shared.database.repository.interfaces.RepositoryContainer;
import me.trae.framework.shared.utility.enums.ChatColor;
import me.trae.framework.shared.utility.enums.TimeUnit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClanManager extends SpigotManager implements IClanManager, RepositoryContainer<ClanRepository> {

    private final Map<String, Clan> CLANS = new HashMap<>();

    public ClanManager(final SpigotPlugin instance) {
        super(instance);

        this.addPrimitive("Max-Squad-Count", 8);
        this.addPrimitive("Max-Claim-Limit", 8);

        this.addPrimitive("Chunk-Outline-Duration", TimeUnit.MINUTES.getDuration() * 2);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new ClanCommand(this));

        // Chat Commands
        addModule(new AllyChatCommand(this));
        addModule(new ClanChatCommand(this));

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
    public Client searchMember(final Clan clan, final Player player, final String name, final boolean inform) {
        final ClientManager clientManager = this.getInstance().getManagerByClass(ClientManager.class);

        final List<Client> list = clan.getMembers().keySet().stream().map(clientManager::getClientByUUID).collect(Collectors.toList());

        final List<Predicate<Client>> predicates = Arrays.asList(
                (client -> client.getName().equalsIgnoreCase(name)),
                (client -> client.getName().toLowerCase().contains(name.toLowerCase()))
        );

        final Function<Client, String> function = (client -> ClanRelation.SELF.getSuffix() + client.getName());

        return UtilSearch.search(Client.class, list, predicates, null, function, "Member Search", player, name, inform);
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
        for (final Chunk chunk : clan.getTerritoryChunks()) {
            this.unOutlineChunk(clan, chunk);
        }

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

        clan.getOnlineMembers().keySet().forEach(player -> this.removeChatType(player.getUniqueId()));
    }

    @Override
    public void removeChatType(final UUID uuid) {
        final Gamer gamer = this.getInstance().getManagerByClass(GamerManager.class).getGamerByUUID(uuid);

        for (final ChatType chatType : ChatType.values()) {
            if (!(gamer.isChatType(chatType))) {
                continue;
            }

            gamer.resetChatType();
            UtilServer.callEvent(new ChatTypeUpdateEvent(gamer, chatType.name()));
            break;
        }
    }

    @Override
    public void messageClan(final Clan clan, final String prefix, final String message, final List<String> variables, final List<UUID> ignore) {
        UtilMessage.simpleMessage(new ArrayList<>(clan.getOnlineMembers().keySet()), prefix, message, variables, null, ignore);
    }

    @Override
    public void messageAllies(final Clan clan, final String prefix, final String message, final List<String> variables, final List<UUID> ignore) {
        for (final Alliance alliance : clan.getAlliances().values()) {
            final Clan allianceClan = this.getClanByName(alliance.getName());
            if (allianceClan == null) {
                continue;
            }

            this.messageClan(allianceClan, prefix, message, variables, ignore);
        }
    }

    @Override
    public void outlineChunk(final Clan clan, final Chunk chunk) {
        this.getInstance().getManagerByClass(BlockRestoreManager.class).outlineChunk(clan.getName(), chunk, Material.GLOWSTONE, this.getPrimitiveCasted(Long.class, "Chunk-Outline-Duration"));
    }

    @Override
    public void unOutlineChunk(final Clan clan, final Chunk chunk) {
        this.getInstance().getManagerByClass(BlockRestoreManager.class).unOutlineChunk(clan.getName(), chunk);
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