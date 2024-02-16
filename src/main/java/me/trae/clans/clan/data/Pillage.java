package me.trae.clans.clan.data;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.interfaces.IPillage;

public class Pillage implements IPillage {

    private final String name;
    private final long systemTime;

    public Pillage(final String name, final long systemTime) {
        this.name = name;
        this.systemTime = System.currentTimeMillis();
    }

    public Pillage(final Clan clan) {
        this(clan.getName(), System.currentTimeMillis());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public long getSystemTime() {
        return this.systemTime;
    }
}