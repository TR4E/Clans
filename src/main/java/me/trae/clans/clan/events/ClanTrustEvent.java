package me.trae.clans.clan.events;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.types.relationship.ClanRelationshipEvent;
import me.trae.clans.clan.events.interfaces.IClanTrustEvent;
import me.trae.core.client.Client;
import org.bukkit.entity.Player;

public abstract class ClanTrustEvent extends ClanRelationshipEvent implements IClanTrustEvent {

    public ClanTrustEvent(final Clan clan, final Player player, final Client client, final Clan target) {
        super(clan, player, client, target);
    }

    public enum Result {
        ACCEPT, FORCE
    }
}