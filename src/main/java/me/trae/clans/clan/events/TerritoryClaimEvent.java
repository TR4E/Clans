package me.trae.clans.clan.events;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.types.player.ClanPlayerCancellableEvent;
import me.trae.core.client.Client;
import me.trae.core.event.types.IChunkEvent;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class TerritoryClaimEvent extends ClanPlayerCancellableEvent implements IChunkEvent {

    private final Chunk chunk;

    public TerritoryClaimEvent(final Clan clan, final Player player, final Client client, final Chunk chunk) {
        super(clan, player, client);

        this.chunk = chunk;
    }

    @Override
    public Chunk getChunk() {
        return this.chunk;
    }
}