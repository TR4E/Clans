package me.trae.clans.clan;

import me.trae.clans.clan.interfaces.IAdminClan;
import org.bukkit.entity.Player;

public class AdminClan extends Clan implements IAdminClan {

    private boolean safe;

    public AdminClan(final String name) {
        super(name);
    }

    public AdminClan(final String name, final Player player) {
        super(name, player);
    }

    @Override
    public String getType() {
        return "Admin Clan";
    }

    @Override
    public boolean isSafe() {
        return this.safe;
    }

    @Override
    public void setSafe(final boolean safe) {
        this.safe = safe;
    }
}