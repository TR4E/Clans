package me.trae.clans.clan.commands.subcommands.abstracts.interfaces;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.core.client.Client;
import org.bukkit.entity.Player;

public interface IClanSubCommand {

    MemberRole getRequiredMemberRole();

    boolean hasRequiredMemberRole(final Player player, final Client client, final Clan clan, final MemberRole requiredMemberRole, final boolean inform);

    default boolean hasRequiredMemberRole(final Player player, final Client client, final Clan clan, final boolean inform) {
        return this.hasRequiredMemberRole(player, client, clan, this.getRequiredMemberRole(), inform);
    }

    void execute(final Player player, final Client client, final Clan clan, final String[] args);
}