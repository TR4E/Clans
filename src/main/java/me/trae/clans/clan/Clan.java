package me.trae.clans.clan;

import me.trae.clans.clan.interfaces.IClan;

public class Clan implements IClan {

    private final String name;

    public Clan(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}