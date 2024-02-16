package me.trae.clans.clan;

import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.interfaces.IClanManager;
import me.trae.clans.clan.modules.HandleChatReceiver;
import me.trae.clans.clan.modules.HandleCustomDeathMessageReceiver;
import me.trae.core.framework.SpigotManager;
import me.trae.core.framework.SpigotPlugin;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClanManager extends SpigotManager implements IClanManager {

    private final Map<String, Clan> CLANS = new HashMap<>();

    public ClanManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new ClanCommand(this));

        // Modules
        addModule(new HandleChatReceiver(this));
        addModule(new HandleCustomDeathMessageReceiver(this));
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
}