package me.trae.clans.clan.events;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.types.player.ClanPlayerCancellableEvent;
import me.trae.clans.clan.events.interfaces.ITerritoryUnClaimEvent;
import me.trae.core.client.Client;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class TerritoryUnClaimEvent extends ClanPlayerCancellableEvent implements ITerritoryUnClaimEvent {

    private final Chunk chunk;
    private final Clan territoryClan;

    public TerritoryUnClaimEvent(final Clan clan, final Player player, final Client client, final Chunk chunk, final Clan territoryClan) {
        super(clan, player, client);

        this.chunk = chunk;
        this.territoryClan = territoryClan;
    }

    @Override
    public Chunk getChunk() {
        return this.chunk;
    }

    @Override
    public Clan getTerritoryClan() {
        return this.territoryClan;
    }
}