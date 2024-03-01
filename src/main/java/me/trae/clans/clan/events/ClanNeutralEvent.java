package me.trae.clans.clan.events;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.types.relationship.ClanRelationshipEvent;
import me.trae.clans.clan.events.interfaces.IClanNeutralEvent;
import me.trae.core.client.Client;
import org.bukkit.entity.Player;

public abstract class ClanNeutralEvent extends ClanRelationshipEvent implements IClanNeutralEvent {

    public ClanNeutralEvent(final Clan clan, final Player player, final Client client, final Clan target) {
        super(clan, player, client, target);
    }

    public enum Result {
        ACCEPT, FORCE
    }
}