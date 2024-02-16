package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.client.Client;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class DisbandCommand extends ClanSubCommand {

    public DisbandCommand(final ClanCommand manager) {
        super(manager, "disband");
    }

    @Override
    public MemberRole getRequiredMemberRole() {
        return MemberRole.LEADER;
    }

    @Override
    public String getDescription() {
        return "Disband the Clan";
    }

    @Override
    public void execute(final Player player, final Client client, final Clan clan, final String[] args) {
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!(this.hasRequiredMemberRole(player, client, clan, true))) {
            return;
        }

        for (final Player target : UtilServer.getOnlinePlayers()) {
            final ClanRelation clanRelation = this.getModule().getManager().getClanRelationByPlayer(target, player);

            UtilMessage.simpleMessage(target, "Clans", "<var> has disbanded <var>", Arrays.asList(clanRelation.getSuffix() + player.getName(), clanRelation.getSuffix() + "Clan " + clan.getName()));
        }

        this.getModule().getManager().removeClan(clan);

        UtilServer.callEvent(new ScoreboardUpdateEvent(player));
    }
}