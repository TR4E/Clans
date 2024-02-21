package me.trae.clans.pillage;

import me.trae.clans.clan.ClanManager;
import me.trae.clans.pillage.interfaces.IPillageManager;
import me.trae.core.framework.SpigotManager;
import me.trae.core.framework.SpigotPlugin;

public class PillageManager extends SpigotManager implements IPillageManager {

    private final ClanManager clanManager;

    public PillageManager(final SpigotPlugin instance) {
        super(instance);

        this.clanManager = this.getInstance().getManagerByClass(ClanManager.class);
    }

    @Override
    public void registerModules() {
    }

    @Override
    public ClanManager getClanManager() {
        return this.clanManager;
    }
}