package me.trae.clans.clan.events;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.types.relationship.ClanRelationshipEvent;
import me.trae.core.client.Client;
import org.bukkit.entity.Player;

public class ClanRevokeTrustEvent extends ClanRelationshipEvent {

    public ClanRevokeTrustEvent(final Clan clan, final Player player, final Client client, final Clan target) {
        super(clan, player, client, target);
    }
}