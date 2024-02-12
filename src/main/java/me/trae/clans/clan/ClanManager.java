package me.trae.clans.clan;

import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.interfaces.IClanManager;
import me.trae.core.framework.SpigotManager;
import me.trae.core.framework.SpigotPlugin;

import java.util.HashMap;
import java.util.Map;

public class ClanManager extends SpigotManager implements IClanManager {

    private final Map<String, Clan> CLANS = new HashMap<>();

    public ClanManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        addModule(new ClanCommand(this));
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
    public boolean isClanByName(final String name) {
        return this.getClans().containsKey(name.toLowerCase());
    }
}