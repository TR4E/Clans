package me.trae.clans.gamer;

import me.trae.clans.gamer.interfaces.IGamer;
import me.trae.framework.shared.gamer.types.LocalGamer;
import me.trae.framework.shared.utility.UtilFormat;

import java.util.UUID;

public class Gamer extends LocalGamer implements IGamer {

    private int coins;

    public Gamer(final UUID uuid) {
        super(uuid);
    }

    @Override
    public int getCoins() {
        return this.coins;
    }

    @Override
    public void setCoins(final int coins) {
        this.coins = coins;
    }

    @Override
    public String getCoinsString() {
        return UtilFormat.toDollar(this.getCoins());
    }
}