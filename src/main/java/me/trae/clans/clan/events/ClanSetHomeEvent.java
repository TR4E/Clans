package me.trae.clans.clan.events;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.types.player.ClanPlayerCancellableEvent;
import me.trae.core.client.Client;
import me.trae.core.event.types.ILocationEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ClanSetHomeEvent extends ClanPlayerCancellableEvent implements ILocationEvent {

    private final Location location;

    public ClanSetHomeEvent(final Clan clan, final Player player, final Client client, final Location location) {
        super(clan, player, client);

        this.location = location;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }
}