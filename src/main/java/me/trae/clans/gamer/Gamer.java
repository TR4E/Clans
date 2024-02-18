package me.trae.clans.gamer;

import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.clans.gamer.interfaces.IGamer;
import me.trae.framework.shared.gamer.types.LocalGamer;
import me.trae.framework.shared.utility.UtilFormat;
import me.trae.framework.shared.utility.interfaces.property.PropertyContainer;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Gamer extends LocalGamer implements IGamer, PropertyContainer<GamerProperty> {

    private int coins;

    public Gamer(final UUID uuid) {
        super(uuid);

        this.coins = 0;
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

    @Override
    public Object getPropertyByValue(final GamerProperty property) {
        if (property == GamerProperty.COINS) {
            return this.getCoins();
        }

        return null;
    }

    @Override
    public List<GamerProperty> getProperties() {
        return Arrays.asList(GamerProperty.values());
    }
}