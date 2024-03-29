package me.trae.clans.clan.events.abstracts.types.relationship;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.types.player.ClanPlayerCancellableEvent;
import me.trae.core.client.Client;
import me.trae.core.event.types.ITargetEvent;
import org.bukkit.entity.Player;

public class ClanRelationshipEvent extends ClanPlayerCancellableEvent implements ITargetEvent<Clan> {

    private final Clan target;

    public ClanRelationshipEvent(final Clan clan, final Player player, final Client client, final Clan target) {
        super(clan, player, client);

        this.target = target;
    }

    @Override
    public Clan getTarget() {
        return this.target;
    }
}